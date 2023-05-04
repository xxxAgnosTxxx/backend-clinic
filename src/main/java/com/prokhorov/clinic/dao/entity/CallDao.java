package com.prokhorov.clinic.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CallDao {
    private String surname;
    private String name;
    private String patron;
    private String date;
    private String phone;
    private String status;
    private String description;
    private String country;
    private String city;
    private String street;
    private String house;
    private Short flat;
    private String statistic;

    public CallDao(String date, String phone, String status, String description, String country, String city, String street, String house, Short flat) {
        this.date = date;
        this.phone = phone;
        this.status = status;
        this.description = description;
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
    }

    public CallDao(String surname, String name, String patron, String date, String phone, String status, String description, String country, String city, String street, String house, Short flat) {
        this.surname = surname;
        this.name = name;
        this.patron = patron;
        this.date = date;
        this.phone = phone;
        this.status = status;
        this.description = description;
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
    }

    public CallDao(String country, String city, String street, String house, Short flat, String phone, String description) {
        this.phone = phone;
        this.description = description;
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
    }
}
