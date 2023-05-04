package com.prokhorov.clinic.service;

import com.prokhorov.clinic.dao.types.RoleType;
import com.prokhorov.clinic.entity.Person;
import com.prokhorov.clinic.repository.AddressesOfPatientRepository;
import com.prokhorov.clinic.repository.EmployeeRepository;
import com.prokhorov.clinic.repository.PatientRepository;
import com.prokhorov.clinic.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PatientRepository patientDataRepository;
    @Autowired
    private EmployeeRepository employeeDataRepository;
    @Autowired
    private AddressesOfPatientRepository addressesOfPatientRepository;

    /**
     * Удаление пустышек, не продолживших регистрацию
     */
    @Scheduled(fixedDelay = 900000)
    public void deleteEmptyPersons() {
        List<Person> emptyPersons = personRepository.findAll().stream()
                .filter(p -> !tokenService.isExist(p))
                .filter(p -> {
                    if (p.getRole().equals(RoleType.PATIENT.getTitle())) {
                        return !patientDataRepository.existsById(p.getPersonId());
                    } else
                        return !employeeDataRepository.existsById(p.getPersonId());
                })
                .collect(Collectors.toList());
        emptyPersons.forEach(e -> addressesOfPatientRepository.deleteAllByPatientId(e.getPersonId()));
        personRepository.deleteAll(emptyPersons);
    }
}
