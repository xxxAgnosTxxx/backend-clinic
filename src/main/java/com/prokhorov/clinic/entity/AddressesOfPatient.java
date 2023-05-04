package com.prokhorov.clinic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Addressesofpatient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressesOfPatient {
    @Id
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "description")
    private String description;
}
