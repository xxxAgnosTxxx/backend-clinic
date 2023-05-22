package com.prokhorov.clinic.dao.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum RoleType {
    PATIENT("patient"),
    EMPLOYEE("employee"),
    ADMIN("admin");

    private final String title;

    public static Optional<RoleType> parseFromString(String string){
        return Arrays.stream(values())
                .filter(type -> type.getTitle().equalsIgnoreCase(string))
                .findFirst();
    }
}