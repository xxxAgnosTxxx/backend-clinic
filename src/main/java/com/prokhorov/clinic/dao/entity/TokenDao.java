package com.prokhorov.clinic.dao.entity;

import com.prokhorov.clinic.entity.Person;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TokenDao {
    private Person person;
    private LocalDateTime startedTime;

    public TokenDao(Person person){
        this.startedTime = LocalDateTime.now();
        this.person = person;
    }
}
