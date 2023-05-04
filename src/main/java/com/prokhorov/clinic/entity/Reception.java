package com.prokhorov.clinic.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Reception")
@Getter
@Setter
/*
    Сущность посещения врача
 */
public class Reception {
    @Id
    @GeneratedValue
    @Column(name = "reception_id")
    private Long id;

    @Column(name = "call_id")
    private Long date;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "therapy")
    private String therapy;
}
