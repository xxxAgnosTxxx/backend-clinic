package com.prokhorov.clinic.repository;

import com.prokhorov.clinic.entity.AddressesOfPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressesOfPatientRepository extends JpaRepository<AddressesOfPatient, Long> {

    @Query("select ap from AddressesOfPatient ap where ap.patientId like :patId")
    List<AddressesOfPatient> findAllByPatientId(Long patId);

    default void deleteAllByPatientId(Long patId){
        deleteAll(findAllByPatientId(patId));
    }

    @Query("select ap from AddressesOfPatient ap where ap.patientId like :patientId and ap.addressId like :addressId")
    Optional<AddressesOfPatient> findByAddressIdAndPatientId(Long addressId, Long patientId);
}
