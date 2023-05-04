package com.prokhorov.clinic.controller.authorization;

import com.prokhorov.clinic.dao.entity.DataPersonDao;
import com.prokhorov.clinic.dao.entity.LoginDao;
import com.prokhorov.clinic.dao.entity.PersonDao;
import com.prokhorov.clinic.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@CrossOrigin
@RequestMapping("v1/authorization")
public class AuthController {
    @Autowired
    private RegistrationService service;

    /**
     * Первичная регистрация
     *
     * @param person
     * @return
     */
    @PostMapping("/register")
    ResponseEntity register(@RequestBody @Valid PersonDao person) {
        return service.registerPerson(person);
    }

    /**
     * Вход
     *
     * @param login
     * @return
     */
    @PostMapping("/signIn")
    ResponseEntity signIn(@RequestBody @Valid LoginDao login) {
        return service.signIn(login);
    }

    /**
     * Вторичная регистрация - заполнение данных
     *
     * @param token
     */
    @PostMapping(value = "/init", consumes = {"application/json"})
    ResponseEntity initialize(@RequestParam UUID token, @RequestBody @Valid DataPersonDao personData) {
        return service.initialize(token, personData);
    }
}
