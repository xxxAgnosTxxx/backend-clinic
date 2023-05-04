package com.prokhorov.clinic.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Person")
@Getter
@Setter
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    @Column(name = "patronymic")
    private String patron;

    @Column(name = "role")
    private String role;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mail")
    private String mail;

    public Person(String surname, String name, String patron, String role, String phone, String mail) {
        this.name = name;
        this.patron = patron;
        this.surname = surname;
        this.role = role;
        this.phone = phone;
        this.mail = mail;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) return false;
        return Objects.equals(((Person) obj).getPersonId(), this.personId);
    }

    public void setAllData(Person person){
        this.setSurname(person.getSurname());
        this.setName(person.getName());
        this.setPatron(person.getPatron());
        this.setRole(person.getRole());
        this.setPhone(person.getPhone());
        this.setMail(person.getMail());
    }
}
