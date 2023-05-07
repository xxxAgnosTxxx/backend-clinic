package com.prokhorov.clinic.controller.employee;

import com.prokhorov.clinic.dao.entity.CallDao;
import com.prokhorov.clinic.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@CrossOrigin
@RequestMapping("v1/employee")
public class EmployeeController {
    @Autowired
    private CallService callService;

    /**
     * Получение всех активных вызовов
     * @return
     */
    @GetMapping("/call")
    public ResponseEntity getActiveCalls() {
        return callService.getActiveCalls();
    }

    /**
     * Получение истории вызова работника скорой
     * @param token
     * @return
     */
    @GetMapping("/call/history")
    public ResponseEntity getHistoryCalls(@RequestParam UUID token){
        return callService.getHistoryCalls(token);
    }

    /**
     * Принятие вызова
     * @param token
     * @param callDao
     * @return
     */
    @PutMapping("/call/accept")
    public ResponseEntity acceptCall(@RequestParam UUID token, @RequestBody CallDao callDao){
        return callService.changeStatusCall(token, callDao);
    }
}
