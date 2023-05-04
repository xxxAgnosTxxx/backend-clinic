package com.prokhorov.clinic.service;

import com.prokhorov.clinic.repository.EmployeeRepository;
import com.prokhorov.clinic.repository.PatientRepository;
import com.prokhorov.clinic.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PatientRepository patientRepository;

}
