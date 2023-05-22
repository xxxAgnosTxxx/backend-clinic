package com.prokhorov.clinic.repository;

import com.prokhorov.clinic.dao.types.CallType;
import com.prokhorov.clinic.entity.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public interface CallRepository extends JpaRepository<Call, Long> {

    @Query("select c from Call c where c.patientId like :patId")
    List<Call> findAllByPersonId(Long patId);

    @Query("select c from Call c where c.patientId like :patId and c.phone like :phone and c.description like :desc")
    List<Call> findByPersonIdPhoneDescription(Long patId, String phone, String desc);

    default List<Call> findByPersonIdPhoneDateDescription(Long patId, String phone, String desc, Timestamp timestamp) {
        return findByPersonIdPhoneDescription(patId, phone, desc).stream()
                .filter(c -> c.getDate().equals(timestamp))
                .collect(Collectors.toList());
    }

    @Query("select c from Call c where c.phone like :phone and c.description like :desc")
    List<Call> findByPhoneDescription(String phone, String desc);

    default List<Call> findByPhoneDateDescription(String phone, String desc, Timestamp timestamp) {
        return findByPhoneDescription(phone, desc).stream()
                .filter(c -> c.getDate().equals(timestamp))
                .collect(Collectors.toList());
    }


    default List<Call> findActiveCalls() {
        return findAll().stream()
                .filter(c -> c.getStatus().equals(CallType.CREATED.getTitle())
                        || c.getStatus().equals(CallType.CONFIRMED.getTitle()))
                .collect(Collectors.toList());
    }

    default List<Call> findLieCalls(Long patientId) {
        return findAllByPersonId(patientId).stream()
                .filter(c -> c.getStatus().equals(CallType.FAILED.getTitle()))
                .collect(Collectors.toList());
    }

    @Query("select c from Call c where c.employeeId like :emp")
    List<Call> findAllByEmployeeId(Long emp);

    @Query("select c from Call c where c.employeeId like :emp and c.phone like :phone and c.description like :desc")
    List<Call> findByEmployeePhoneDateDescription(Long emp, String phone, String desc);
}
