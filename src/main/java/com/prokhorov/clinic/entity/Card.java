package com.prokhorov.clinic.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Card")
@Getter
@Setter
/*
    Сущность записи в медкарте пациента
 */
public class Card {
    @Column(name = "person_id")
    private Long personId;

    @Id
    @Column(name = "reception_id")
    private Long receptionId;
}
