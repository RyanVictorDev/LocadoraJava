package com.locadora.springboot.dashboard.controllers;

import com.locadora.springboot.dashboard.services.DashboardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard")
public class DashboardController {

    @Autowired
    DashboardServices dashboardServices;

    @GetMapping("/rentsQuantity")
    public ResponseEntity<Integer> getRentsQuantity(){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getNumberOfRentals());
    }

    @GetMapping("/rentsLateQuantity")
    public ResponseEntity<Integer> getRentsLateQuantity(){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getNumberOfRentalsLate());
    }

    @GetMapping("/deliveredInTimeQuantity")
    public ResponseEntity<Integer> getRentsDeliveredInTime(){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getDeliveredInTime());
    }

    @GetMapping("/deliveredWithDelayQuantity")
    public ResponseEntity<Integer> getRentsDeliveredWithDelay(){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getDeliveredWithDelay());
    }
}
