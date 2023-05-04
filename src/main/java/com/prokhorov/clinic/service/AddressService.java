package com.prokhorov.clinic.service;

import com.prokhorov.clinic.dao.entity.AddressDao;
import com.prokhorov.clinic.entity.Address;
import com.prokhorov.clinic.entity.AddressesOfPatient;
import com.prokhorov.clinic.entity.Person;
import com.prokhorov.clinic.mapper.EntityMapper;
import com.prokhorov.clinic.repository.AddressRepository;
import com.prokhorov.clinic.repository.AddressesOfPatientRepository;
import com.prokhorov.clinic.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressesOfPatientRepository addressesOfPatientRepository;

    public ResponseEntity setAddress(UUID token, AddressDao dao) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person patient = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        Address address;
        if (dao.getFlatNum() == null)
            address = addressRepository.findAddress(dao.getCountry(), dao.getCity(), dao.getStreet(), dao.getHouseNum())
                    .orElse(new Address(dao.getCountry(), dao.getCity(), dao.getStreet(), dao.getHouseNum(), dao.getFlatNum()));
        else
            address = addressRepository.findAddress(dao.getCountry(), dao.getCity(), dao.getStreet(), dao.getHouseNum(), dao.getFlatNum())
                    .orElse(new Address(dao.getCountry(), dao.getCity(), dao.getStreet(), dao.getHouseNum(), dao.getFlatNum()));
        if (address.getId() == null) address = addressRepository.save(address);
        addressesOfPatientRepository.save(new AddressesOfPatient(address.getId(), patient.getPersonId(), null));
        return ResponseEntity.ok().body(tokenService.getToken(patient));
    }

    public ResponseEntity getAddress(UUID token) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person patient = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        List<AddressesOfPatient> addressesOfPatientList = addressesOfPatientRepository.findAllByPatientId(patient.getPersonId());
        List<Address> addresses = addressesOfPatientList.stream()
                .map(ap -> addressRepository.findById(ap.getAddressId()).orElse(null))
                .filter(ad -> !Objects.isNull(ad))
                .collect(Collectors.toList());
        return ResponseEntity.ok(addresses.stream()
                .map(EntityMapper::entityToDao));
    }

    public ResponseEntity deleteAddress(UUID token, AddressDao dao) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person patient = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        Address address;
        if (dao.getFlatNum() == null)
            address = addressRepository.findAddress(dao.getCountry(), dao.getCity(), dao.getStreet(), dao.getHouseNum()).get();
        else
            address = addressRepository.findAddress(dao.getCountry(), dao.getCity(), dao.getStreet(), dao.getHouseNum(), dao.getFlatNum()).get();
        addressesOfPatientRepository.delete(addressesOfPatientRepository.findByAddressIdAndPatientId(address.getId(), patient.getPersonId()).get());
        return ResponseEntity.ok(tokenService.getToken(patient));
    }
}
