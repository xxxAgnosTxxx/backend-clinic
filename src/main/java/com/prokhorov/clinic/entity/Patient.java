package com.prokhorov.clinic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Patient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/*
    Сущность пациента
 */
public class Patient {
    @Id
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "snils")
    private String snils;

    @Column(name = "polis")
    private String polis;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "password")
    private String password;

    public void setAllData(Patient patient){
        this.setSnils(patient.getSnils());
        this.setPolis(patient.getPolis());
        this.setPassword(patient.getPassword());
        this.setBirth(patient.getBirth());
    }

    public Patient(Long id){
        this.personId = id;
    }
}
