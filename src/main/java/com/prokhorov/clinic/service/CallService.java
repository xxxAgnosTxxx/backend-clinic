package com.prokhorov.clinic.service;

import com.prokhorov.clinic.dao.entity.CallDao;
import com.prokhorov.clinic.dao.types.CallType;
import com.prokhorov.clinic.dao.types.RoleType;
import com.prokhorov.clinic.entity.Address;
import com.prokhorov.clinic.entity.Call;
import com.prokhorov.clinic.entity.Person;
import com.prokhorov.clinic.mapper.EntityMapper;
import com.prokhorov.clinic.repository.AddressRepository;
import com.prokhorov.clinic.repository.CallRepository;
import com.prokhorov.clinic.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CallService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CallRepository callRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PersonRepository personRepository;

    public ResponseEntity createUnregisteredCall(CallDao dao) {
        Address address = createAndSaveAddress(dao);
        Call call = new Call(new Timestamp(System.currentTimeMillis()), CallType.CREATED.getTitle(), address.getId(), dao.getDescription(), dao.getPhone());
        callRepository.save(call);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity createRegisteredCall(UUID token, CallDao dao) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person person = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        Address address = createAndSaveAddress(dao);
        Call call = new Call(person.getPersonId(), new Timestamp(System.currentTimeMillis()), CallType.CONFIRMED.getTitle(), address.getId(), dao.getDescription(), dao.getPhone());
        callRepository.save(call);
        return ResponseEntity.ok(tokenService.getToken(person));
    }

    private Address createAndSaveAddress(CallDao dao) {
        Address address;
        if (dao.getFlat() == null) {
            address = addressRepository.findAddress(dao.getCountry().trim(), dao.getCity().trim(), dao.getStreet().trim(), dao.getHouse().trim())
                    .orElse(new Address(dao.getCountry(), dao.getCity(), dao.getStreet(), dao.getHouse()));
        } else {
            address = addressRepository.findAddress(dao.getCountry().trim(), dao.getCity().trim(), dao.getStreet().trim(), dao.getHouse().trim(), dao.getFlat())
                    .orElse(new Address(dao.getCountry(), dao.getCity(), dao.getStreet(), dao.getHouse(), dao.getFlat()));
        }

        if (address.getId() == null) {
            address = addressRepository.save(address);
        }
        return address;
    }

    public ResponseEntity getCalls(UUID token) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person person = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        List<CallDao> calls = callRepository.findAllByPersonId(person.getPersonId()).stream()
                .sorted(Comparator.comparing(Call::getDate).reversed())
                .map(c -> EntityMapper.entityToDao(person, c, addressRepository.findById(c.getAddressId()).get()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(calls);
    }

    public ResponseEntity cancelCall(UUID token, CallDao dao) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person person = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        Timestamp date = getTimestampFromString(dao.getDate());
        Call cancelingCall = filterActiveCalls(callRepository.findByPersonIdPhoneDateDescription(person.getPersonId(), dao.getPhone(), dao.getDescription(), date));
        cancelingCall.setStatus(CallType.CANCELED.getTitle());
        callRepository.save(cancelingCall);
        return ResponseEntity.ok(tokenService.getToken(person));
    }

    public ResponseEntity getActiveCalls() {
        List<CallDao> calls = convertAllKindOfCalls(callRepository.findActiveCalls()).stream()
                .sorted((x, y) -> {
                    int dateCompare = LocalDateTime.parse(x.getDate().replace(" ", "T"))
                            .compareTo(LocalDateTime.parse(y.getDate().replace(" ", "T")));
                    String statX = x.getStatistic();
                    if (statX == null) {
                        if (!Objects.equals(y.getStatistic(), statX)) return 1;
                        else return dateCompare;
                    } else {
                        if (y.getStatistic() == null) return -1;
                        else {
                            Integer statXInt = Integer.parseInt(statX.substring(0, statX.length() - 1));
                            Integer statYInt = Integer.parseInt(y.getStatistic().substring(0, statX.length() - 1));
                            return statXInt.compareTo(statYInt);
                        }
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(calls);
    }

    private Timestamp getTimestampFromString(String input) {
        LocalDate date = LocalDate.parse(input.split(" ")[0]);
        LocalTime time = LocalTime.parse(input.split(" ")[1]);
        LocalDateTime ldt = LocalDateTime.of(date, time);
        return Timestamp.valueOf(ldt);
    }

    private List<CallDao> convertAllKindOfCalls(List<Call> calls) {
        return calls.stream()
                .map(c -> {
                    Long patId = c.getPatientId();
                    Long empId = c.getEmployeeId();
                    if (patId == null) {
                        if (empId != null) {
                            return EntityMapper.entityToDao(c, addressRepository.findById(c.getAddressId()).get(), personRepository.findById(empId).get());
                        }
                        return EntityMapper.entityToDao(c, addressRepository.findById(c.getAddressId()).get());
                    } else {
                        if (empId != null) {
                            String statistic = (callRepository.findLieCalls(patId).size() * 100) / callRepository.findAllByPersonId(patId).size() + "%";
                            return EntityMapper.entityToDao(personRepository.findById(c.getPatientId()).get(), c,
                                    addressRepository.findById(c.getAddressId()).get(), statistic, personRepository.findById(empId).get());
                        }
                        String statistic = (callRepository.findLieCalls(patId).size() * 100) / callRepository.findAllByPersonId(patId).size() + "%";
                        return EntityMapper.entityToDao(personRepository.findById(c.getPatientId()).get(), c,
                                addressRepository.findById(c.getAddressId()).get(), statistic);
                    }
                })
                .collect(Collectors.toList());
    }

    public ResponseEntity getHistoryCalls(UUID token) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person person = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        List<CallDao> calls = convertAllKindOfCalls(callRepository.findAllByEmployeeId(person.getPersonId()))
                .stream()
                .sorted(this::compareByTimestamp)
                .collect(Collectors.toList());
        return ResponseEntity.ok(calls);
    }

    public ResponseEntity getActiveCalls(UUID token){
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person person = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        List<CallDao> activeCalls = convertAllKindOfCalls(callRepository.findAllByEmployeeId(person.getPersonId())).stream()
                .filter(c -> c.getStatus().equals(CallType.IN_PROGRESS.getTitle()))
                .sorted(this::compareByTimestamp)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activeCalls);
    }

    public ResponseEntity changeStatusCall(UUID token, CallDao callDao) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person employee = personRepository.findById(tokenService.getPerson(token).getPersonId()).get();
        Timestamp date = getTimestampFromString(callDao.getDate());
        Call call = filterActiveCalls(callRepository.findByPhoneDateDescription(callDao.getPhone(), callDao.getDescription(), date));
        call.setStatus(CallType.parseFromString(callDao.getStatus()).get().getTitle());
        call.setEmployeeId(employee.getPersonId());
        //время приема и завершения вызовов
        CallType type = CallType.parseFromString(callDao.getStatus()).get();
        if (type.equals(CallType.IN_PROGRESS)) {
            call.setAcceptDate(Timestamp.from(Instant.now()));
        } else if (CallType.getFinishedCallTypes().contains(type)) {
            call.setEndDate(Timestamp.from(Instant.now()));
        }
        callRepository.save(call);
        return ResponseEntity.ok(tokenService.getToken(employee));
    }

    private Call filterActiveCalls(List<Call> calls) {
        return calls.stream()
                .filter(c -> c.getStatus().equals(CallType.CREATED.getTitle())
                        || c.getStatus().equals(CallType.CONFIRMED.getTitle())
                        || c.getStatus().equals(CallType.IN_PROGRESS.getTitle()))
                .findFirst().get();
    }

    public ResponseEntity setPayCall(UUID token, CallDao dao) {
        if (tokenService.isOldToken(token))
            return ResponseEntity.badRequest().body("Время ожидания истекло. Войдите заново.");
        Person employee = personRepository.findOneBySurnameNamePatronRole(dao.getSurnameEmp(), dao.getNameEmp(), dao.getPatronEmp(), RoleType.EMPLOYEE.getTitle()).get();
        Timestamp callDate = getTimestampFromString(dao.getDate());
        Call call = callRepository.findByEmployeePhoneDateDescription(employee.getPersonId(), dao.getPhone(), dao.getDescription()).stream()
                .filter(c -> c.getDate().equals(callDate))
                .findFirst().get();
        call.setIsPaid(true);
        callRepository.save(call);
        return ResponseEntity.ok().build();
    }

    private int compareByTimestamp(CallDao callDao1, CallDao callDao2) {
        LocalDateTime ld1 = LocalDateTime.parse(callDao1.getDate().replace(" ", "T"));
        LocalDateTime ld2 = LocalDateTime.parse(callDao2.getDate().replace(" ", "T"));
        return ld2.compareTo(ld1);
    }
}
