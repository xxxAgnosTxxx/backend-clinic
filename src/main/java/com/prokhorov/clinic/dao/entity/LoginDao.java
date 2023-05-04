package com.prokhorov.clinic.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class LoginDao {
    @NotNull
    @NotEmpty
    private String login;
    @NotEmpty
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String role;
}
