package com.prokhorov.clinic.repository;

import com.prokhorov.clinic.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    default Person findByPhoneAndType(String phone, String type) {
        return findPatientByPhone(phone).stream()
                .filter(p -> p.getRole().equals(type))
                .findFirst()
                .orElse(null);
    }

    @Query("select p from Person p where p.phone like :phone")
    List<Person> findPatientByPhone(String phone);

    @Query("select p from Person p where p.surname like :surname and p.name like :name and p.patron like :patron and p.phone like :phone")
    Optional<Person> findOneBySurnameNamePatronPhone(String surname, String name, String patron, String phone);

    @Query("select p from Person p where p.surname like :surname and p.name like :name and p.patron like :patron and p.role like :role")
    Optional<Person> findOneBySurnameNamePatronRole(String surname, String name, String patron, String role);
}
