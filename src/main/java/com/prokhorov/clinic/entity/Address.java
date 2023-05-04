package com.prokhorov.clinic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/*
    Сущность адреса пациента
 */
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house_num")
    private String houseNum;

    @Column(name = "flat_num")
    private Short flatNum;

    public Address(String country, String city, String street, String houseNum) {
        this.country = country.trim();
        this.city = city.trim();
        this.street = street.trim();
        this.houseNum = houseNum.trim();
    }

    public Address(String country, String city, String street, String houseNum, Short flatNum) {
        this(country, city, street, houseNum);
        this.flatNum = flatNum;
    }

}
