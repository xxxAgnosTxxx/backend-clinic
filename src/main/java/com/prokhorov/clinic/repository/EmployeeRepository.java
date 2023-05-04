package com.prokhorov.clinic.repository;

import com.prokhorov.clinic.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select ed from Employee ed where ed.login like :login and ed.password like :password")
    Optional<Employee> findByAuthIdAndPassword(String login, String password);

    @Query("select e from Employee e where e.active like 1")
    List<Employee> findAllActive();
}
