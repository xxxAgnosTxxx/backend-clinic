package com.prokhorov.clinic.service;

import com.prokhorov.clinic.dao.entity.DataPersonDao;
import com.prokhorov.clinic.dao.entity.LoginDao;
import com.prokhorov.clinic.dao.entity.PersonDao;
import com.prokhorov.clinic.dao.types.RoleType;
import com.prokhorov.clinic.entity.Employee;
import com.prokhorov.clinic.entity.Patient;
import com.prokhorov.clinic.entity.Person;
import com.prokhorov.clinic.mapper.EntityMapper;
import com.prokhorov.clinic.repository.EmployeeRepository;
import com.prokhorov.clinic.repository.PatientRepository;
import com.prokhorov.clinic.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EmployeeRepository employeeDataRepository;
    @Autowired
    private PatientRepository patientDataRepository;
    @Autowired
    private TokenService tokenService;

    public ResponseEntity registerPerson(PersonDao dao) {
        RoleType type = RoleType.parseFromString(dao.getRole()).get();
        String role = type.getTitle();
        UUID personId;
        if (Objects.nonNull(personRepository.findByPhoneAndType(dao.getPhone(), role))) {
            return ResponseEntity.badRequest().body("Пользователь с таким номером телефона уже зарегистрирован.");
        } else if (!Objects.isNull(personRepository.findByPhone(dao.getPhone()))) {
            Person person = personRepository.findByPhone(dao.getPhone());
            person.setRole(person.getRole() + ";" + role);
            personRepository.save(person);
            personId = tokenService.getToken(person);
            return ResponseEntity.ok().body(personId.toString());
        } else {
            Person person = personRepository.save(EntityMapper.daoToEntity(dao));
            personId = tokenService.getToken(person);
            return ResponseEntity.ok().body(personId.toString());
        }
    }

    public ResponseEntity signIn(LoginDao dao) {
        RoleType type = RoleType.parseFromString(dao.getRole()).get();
        Person person;
        if (type.equals(RoleType.EMPLOYEE)) {
            Optional<Employee> employeeData = employeeDataRepository.findByAuthIdAndPassword(dao.getLogin(), dao.getPassword());
            if (!employeeData.isPresent()) {
                return ResponseEntity.badRequest().body("Неверный логин или пароль.");
            }
            person = personRepository.findById(employeeData.get().getPersonId()).orElse(null);
            if (Objects.nonNull(person)) {
                return ResponseEntity.ok().body(tokenService.getToken(person));
            }
        } else if (type.equals(RoleType.PATIENT)){
            Optional<Patient> patientData = patientDataRepository.findByPolisAndPassword(dao.getLogin(), dao.getPassword());
            if (!patientData.isPresent()) {
                return ResponseEntity.badRequest().body("Неверный логин или пароль.");
            }
            person = personRepository.findById(patientData.get().getPersonId()).orElse(null);
            if (Objects.nonNull(person)) {
                return ResponseEntity.ok().body(tokenService.getToken(person));
            }
        }
        return ResponseEntity.badRequest().body("Неизвестная ошибка входа.");
    }

    public ResponseEntity initialize(UUID token, DataPersonDao personData) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время на авторизацию истекло - обратитесь к администратору.");
        Person person = tokenService.getPerson(token);
        if (person.getRole().equals(RoleType.EMPLOYEE.getTitle())) {
            if (employeeDataRepository.existsById(person.getPersonId()))
                return ResponseEntity.badRequest().body("Вы уже зарегистрированы.");
            Employee data = new Employee(person.getPersonId(),true, personData.getPassword(), personData.getLogin());
            employeeDataRepository.save(data);
            return ResponseEntity.ok().body(token);
        } else {
            if (patientDataRepository.existsById(person.getPersonId()))
                return ResponseEntity.badRequest().body("Вы уже зарегистрированы. Изменить данные можно в личном кабинете.");
            Patient data = new Patient(person.getPersonId(), personData.getSnils(), personData.getPolis(), personData.getBirth(), personData.getPassword());
            patientDataRepository.save(data);
            return ResponseEntity.ok().body(token);
        }
    }
}
