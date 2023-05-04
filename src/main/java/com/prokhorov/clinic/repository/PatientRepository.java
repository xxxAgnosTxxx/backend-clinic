package com.prokhorov.clinic.repository;

import com.prokhorov.clinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("select pd from Patient pd where pd.password like :password and pd.polis like :polis")
    Optional<Patient> findByPolisAndPassword(String polis, String password);

    @Query("select p from Patient p where p.polis like :polis")
    Optional<Patient> findByPolis(String polis);

    @Query("select p from Patient p where p.snils like :snils")
    Optional<Patient> findBySnils(String snils);

}
