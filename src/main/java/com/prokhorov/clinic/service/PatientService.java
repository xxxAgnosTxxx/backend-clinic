package com.prokhorov.clinic.service;

import com.prokhorov.clinic.dao.entity.AddressDao;
import com.prokhorov.clinic.dao.entity.DataPersonDao;
import com.prokhorov.clinic.entity.Address;
import com.prokhorov.clinic.entity.Call;
import com.prokhorov.clinic.entity.Patient;
import com.prokhorov.clinic.entity.Person;
import com.prokhorov.clinic.mapper.EntityMapper;
import com.prokhorov.clinic.repository.AddressRepository;
import com.prokhorov.clinic.repository.CallRepository;
import com.prokhorov.clinic.repository.PatientRepository;
import com.prokhorov.clinic.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {
    @Autowired
    private CallRepository callRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PatientRepository patientRepository;

    public ResponseEntity getData(UUID token) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person person = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        Patient patient = patientRepository.findById(person.getPersonId()).orElse(new Patient());
        return ResponseEntity.ok().body(EntityMapper.entityToDao(person, patient));
    }

    public ResponseEntity setData(UUID token, DataPersonDao dao) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person person = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        //проверка на существующие снилс и полис другого человека
        Optional<Patient> pO1 = patientRepository.findByPolis(dao.getPolis());
        Optional<Patient> pO2 = patientRepository.findBySnils(dao.getSnils());
        if(pO1.isPresent() && !pO1.get().getPersonId().equals(person.getPersonId())){
            return ResponseEntity.badRequest().body("Номер полиса зарегистрирован на другого пациента");
        }
        if(pO2.isPresent() && !pO2.get().getPersonId().equals(person.getPersonId())){
            return ResponseEntity.badRequest().body("Номер СНИЛСа зарегистрирован на другого пациента");
        }
                
        person.setAllData(EntityMapper.daoToEntity(dao.getPerson()));
        personRepository.save(person);
        Patient patient = patientRepository.findById(person.getPersonId()).orElse(new Patient(person.getPersonId()));
        patient.setAllData(EntityMapper.daoToEntity(dao));
        patientRepository.save(patient);
        return ResponseEntity.ok(tokenService.getToken(person));
    }
}
