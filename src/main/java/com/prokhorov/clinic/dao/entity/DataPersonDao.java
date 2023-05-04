package com.prokhorov.clinic.dao.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DataPersonDao {
    private PersonDao person;
    private String password;
    //для сотрудника
    private String speciality;
    private String position;
    private String login;
    //для пациента
    private String snils;
    private String polis;
    private LocalDate birth;
}
