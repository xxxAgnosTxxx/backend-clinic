package com.prokhorov.clinic.dao.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

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

    public static EnumSet<CallType> getFinishedCallTypes() {
        return EnumSet.of(
                FINISHED,
                CANCELED,
                FAILED,
                HOSPITALIZED);
    }

    public static Optional<CallType> parseFromString(String string) {
        return Arrays.stream(values())
                .filter(type -> type.getTitle().equalsIgnoreCase(string))
                .findFirst();
    }
}
