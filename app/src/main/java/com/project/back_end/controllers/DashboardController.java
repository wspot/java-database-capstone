package com.project.back_end.controllers;

import com.project.back_end.services.Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {
    Service service;

    public DashboardController(Service service) {
        this.service = service;
    }

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {

        String tokenValidation = service.validateToken(token, "admin");
        if (tokenValidation.isEmpty()) {
            return "admin/adminDashboard";
        }
        else{
            return "redirect:/";
        }
    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard (@PathVariable String token) {

        String tokenValidation = service.validateToken(token, "doctor");
        if (tokenValidation.isEmpty()) {
            return "doctor/doctorDashboard";
        }
        else{
            return "redirect:/";
        }
    }
}
