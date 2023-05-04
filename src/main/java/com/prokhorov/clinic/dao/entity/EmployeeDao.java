package com.prokhorov.clinic.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class EmployeeDao {
    @NotNull
    @NotEmpty
    private String speciality;
    @NotNull
    @NotEmpty
    private String position;
    private String education;
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String surname;
    @NotEmpty
    @NotNull
    private String patronymic;
}
