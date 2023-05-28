package com.prokhorov.clinic.controller.patient;

import com.prokhorov.clinic.dao.entity.AddressDao;
import com.prokhorov.clinic.dao.entity.CallDao;
import com.prokhorov.clinic.dao.entity.DataPersonDao;
import com.prokhorov.clinic.service.AddressService;
import com.prokhorov.clinic.service.CallService;
import com.prokhorov.clinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@CrossOrigin
@RequestMapping("v1/patient")
public class PatientController {
    @Autowired
    private PatientService service;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CallService callService;

    /**
     * Добавление/изменение адреса
     */
    @PostMapping("/address")
    public ResponseEntity setAddress(@RequestParam UUID token, @RequestBody @Valid AddressDao dao) {
        return addressService.setAddress(token, dao);
    }

    /**
     * Адреса пациента
     * @param token
     * @return
     */
    @GetMapping("/address")
    public ResponseEntity getAddress(@RequestParam UUID token) {
        return addressService.getAddress(token);
    }

    /**
     * Удаление адреса
     * @param token
     * @param dao
     * @return
     */
    @DeleteMapping("/address")
    public ResponseEntity deleteAddress(@RequestParam UUID token, @RequestBody AddressDao dao){
        return addressService.deleteAddress(token, dao);
    }

    /**
     * Данные пациента
     */
    @GetMapping("/profile")
    public ResponseEntity getData(@RequestParam UUID token) {
        return service.getData(token);
    }

    /**
     * Смена данных пациента
     *
     * @param token
     * @param dao
     * @return
     */
    @PostMapping("/profile")
    public ResponseEntity setData(@RequestParam UUID token, @RequestBody DataPersonDao dao) {
        return service.setData(token, dao);
    }

    /**
     * Создание вызова без авторизации
     *
     * @return
     */
    @PostMapping("/call")
    public ResponseEntity createCall(@RequestBody CallDao dao){
        return callService.createUnregisteredCall(dao);
    }

    /**
     * Создание вызова из ЛК
     *
     * @return
     */
    @PostMapping("/call/reg")
    public ResponseEntity createRegisteredCall(@RequestParam UUID token, @RequestBody CallDao dao){
        return callService.createRegisteredCall(token, dao);
    }

    /**
     * Получение вызовов
     */
    @GetMapping("/call/reg")
    public ResponseEntity getCalls(@RequestParam UUID token){
        return callService.getCalls(token);
    }

    /**
     * Отмена вызова
     * @param token
     * @param dao
     * @return
     */
    @PostMapping("call/reg/cancel")
    public ResponseEntity cancelCall(@RequestParam UUID token, @RequestBody CallDao dao){
        return callService.cancelCall(token, dao);
    }
}
