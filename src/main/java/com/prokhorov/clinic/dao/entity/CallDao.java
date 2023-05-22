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
    private String surnameEmp;
    private String nameEmp;
    private String patronEmp;
    private Boolean isPaid;

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
        this(date, phone, status, description, country, city, street, house, flat);
        this.surname = surname;
        this.name = name;
        this.patron = patron;
    }

    public CallDao(String surname, String name, String patron, String date, String phone, String status, String description, String country, String city, String street, String house, Short flat, String statistic) {
        this(surname, name, patron, date, phone, status, description, country, city, street, house, flat);
        this.statistic = statistic;
    }

    public CallDao(String date, String phone, String status, String description, String country, String city, String street, String house, Short flat, String surnameEmp, String nameEmp, String patronEmp, Boolean isPaid) {
        this(date, phone, status, description, country, city, street, house, flat);
        this.surnameEmp = surnameEmp;
        this.nameEmp = nameEmp;
        this.patronEmp = patronEmp;
        this.isPaid = isPaid;
    }
}
