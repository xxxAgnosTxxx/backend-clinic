package com.prokhorov.clinic.dao.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum CallType {
    CREATED("Создан"),
    CONFIRMED("Подтвержден"),
    FINISHED("Завершен"),
    CANCELED("Отменен"),
    FAILED("Ложный вызов"),
    HOSPITALIZED("Госпитализирован"),
    IN_PROGRESS("Принят");

    private final String title;

    public static Optional<CallType> parseFromString(String string){
        return Arrays.stream(values())
                .filter(type -> type.getTitle().equalsIgnoreCase(string))
                .findFirst();
    }
}
