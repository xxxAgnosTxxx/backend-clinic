package com.prokhorov.clinic.controller.person;

import com.prokhorov.clinic.dao.entity.CallFilterDao;
import com.prokhorov.clinic.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@CrossOrigin
@RequestMapping("v1/person")
public class PersonController {
    @Autowired
    private CallService callService;

    @GetMapping("/filter")
    public ResponseEntity getFilters(@RequestParam UUID token){
        return callService.getFilters(token);
    }

    @PostMapping("/filter")
    public ResponseEntity getFilteredCalls(@RequestParam UUID token, @RequestBody CallFilterDao filterDao){
        return callService.filterCalls(token, filterDao);
    }
}
