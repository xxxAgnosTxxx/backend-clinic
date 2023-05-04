package com.prokhorov.clinic.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class PersonDao {
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String surname;
    @NotEmpty
    @NotNull
    private String patronymic;
    @NotEmpty
    @NotNull
    private String role;
    @NotEmpty
    @NotNull
    private String phone;
    private String mail;
}
