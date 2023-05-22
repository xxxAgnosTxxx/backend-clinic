package com.prokhorov.clinic.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Call")
@Getter
@Setter
@NoArgsConstructor
/*
    Сущность вызова врача на дом
 */
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "date_reg")
    private Timestamp date;

    @Column(name = "status")
    private String status;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "description")
    private String description;

    @Column(name = "phone")
    private String phone;

    @Column(name = "paid")
    private Boolean isPaid;

    public Call(Timestamp date, String status, Long addressId, String description, String phone) {
        this.date = date;
        this.status = status;
        this.addressId = addressId;
        this.description = description;
        this.phone = phone;
    }

    public Call(Long patientId, Timestamp date, String status, Long addressId, String description, String phone) {
        this.patientId = patientId;
        this.date = date;
        this.status = status;
        this.addressId = addressId;
        this.description = description;
        this.phone = phone;
    }
}
