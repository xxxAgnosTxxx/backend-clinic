package com.prokhorov.clinic.mapper;

import com.prokhorov.clinic.dao.entity.*;
import com.prokhorov.clinic.dao.types.RoleType;
import com.prokhorov.clinic.entity.Address;
import com.prokhorov.clinic.entity.Call;
import com.prokhorov.clinic.entity.Patient;
import com.prokhorov.clinic.entity.Person;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class EntityMapper {
    public static Person daoToEntity(PersonDao dao) {
        Optional<RoleType> role = RoleType.parseFromString(dao.getRole());
        return role.map(personType -> new Person(dao.getSurname(), dao.getName(), dao.getPatronymic(), personType.getTitle(), dao.getPhone(), dao.getMail(), dao.getSex()))
                .orElse(null);
    }

    public static Address daoToEntity(AddressDao dao) {
        return new Address(dao.getCountry(), dao.getCity(), dao.getStreet(), dao.getHouseNum(), dao.getFlatNum());
    }

    public static PersonDao entityToDao(Person person) {
        return new PersonDao(person.getName(), person.getSurname(), person.getPatron(), person.getRole(), person.getPhone(), person.getMail(), person.getSex());
    }

    public static DataPersonDao entityToDao(Person person, Patient patient) {
        DataPersonDao dao = new DataPersonDao();
        dao.setPerson(entityToDao(person));
        dao.setPassword(patient.getPassword());
        dao.setPolis(patient.getPolis());
        dao.setSnils(patient.getSnils());
        dao.setBirth(patient.getBirth());
        return dao;
    }

    public static Patient daoToEntity(DataPersonDao dao) {
        return new Patient(null, dao.getSnils(), dao.getPolis(), dao.getBirth(), dao.getPassword());
    }

    public static AddressDao entityToDao(Address address) {
        return new AddressDao(address.getCountry(), address.getCity(), address.getStreet(), address.getHouseNum(), address.getFlatNum());
    }

    /**
     * вызов в лк пациента
     * @param person
     * @param call
     * @param address
     * @return
     */
    public static CallDao entityToDao(Person person, Call call, Address address) {
        String date = parseCallDateToString(call.getDate());
        return new CallDao(person.getSurname(), person.getName(), person.getPatron(), date, call.getPhone(), call.getStatus(), call.getDescription(),
                address.getCountry(), address.getCity(), address.getStreet(), address.getHouseNum(), address.getFlatNum());
    }

    /**
     * вызов на вкладке приема сотрудника
     * @param person
     * @param call
     * @param address
     * @param stat
     * @return
     */
    public static CallDao entityToDao(Person person, Call call, Address address, String stat) {
        String date = parseCallDateToString(call.getDate());
        return new CallDao(person.getSurname(), person.getName(), person.getPatron(), date, call.getPhone(), call.getStatus(), call.getDescription(),
                address.getCountry(), address.getCity(), address.getStreet(), address.getHouseNum(), address.getFlatNum(), stat);
    }

    /**
     * анонимный вызов
     * @param call
     * @param address
     * @return
     */
    public static CallDao entityToDao(Call call, Address address) {
        String date = parseCallDateToString(call.getDate());
        return new CallDao(date, call.getPhone(), call.getStatus(), call.getDescription(), address.getCountry(), address.getCity(), address.getStreet(), address.getHouseNum(), address.getFlatNum());
    }

    /**
     * вызов для диспетчера от зарегестрированного пользователя
     * @param person
     * @param call
     * @param address
     * @param stat
     * @param employee
     * @return
     */
    public static CallDao entityToDao(Person person, Call call, Address address, String stat, Person employee) {
        String date = parseCallDateToString(call.getDate());
        return new CallDao(person.getSurname(), person.getName(), person.getPatron(), date, call.getPhone(), call.getStatus(),
                call.getDescription(), address.getCountry(), address.getCity(), address.getStreet(), address.getHouseNum(),
                address.getFlatNum(), stat, employee.getSurname(), employee.getName(), employee.getPatron(), call.getIsPaid(), null, null);
    }

    /**
     * анонимный вызов для диспетчера
     * @param call
     * @param address
     * @param employee
     * @return
     */
    public static CallDao entityToDao(Call call, Address address, Person employee) {
        String date = parseCallDateToString(call.getDate());
        return new CallDao(date, call.getPhone(), call.getStatus(), call.getDescription(), address.getCountry(), address.getCity(),
                address.getStreet(), address.getHouseNum(), address.getFlatNum(), employee.getSurname(), employee.getName(),
                employee.getPatron(), call.getIsPaid());
    }

    private static String parseCallDateToString(Timestamp callDate) {
        LocalDateTime timestamp = callDate.toLocalDateTime();
        return timestamp.toLocalDate() + " " + timestamp.toLocalTime();
    }
}
