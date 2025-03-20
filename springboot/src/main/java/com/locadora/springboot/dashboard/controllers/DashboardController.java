package com.locadora.springboot.dashboard.controllers;

import com.locadora.springboot.dashboard.services.DashboardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("dashboard")
public class DashboardController {

    @Autowired
    DashboardServices dashboardServices;

    @GetMapping("/rentsQuantity")
    public ResponseEntity<Integer> getRentsQuantity(int numberOfMonths){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getNumberOfRentals(numberOfMonths));
    }

    @GetMapping("/rentsLateQuantity")
    public ResponseEntity<Integer> getRentsLateQuantity(int numberOfMonths){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getNumberOfRentalsLate(numberOfMonths));
    }

    @GetMapping("/deliveredInTimeQuantity")
    public ResponseEntity<Integer> getRentsDeliveredInTime(int numberOfMonths){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getDeliveredInTime(numberOfMonths));
    }

    @GetMapping("/deliveredWithDelayQuantity")
    public ResponseEntity<Integer> getRentsDeliveredWithDelay(int numberOfMonths){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getDeliveredWithDelay(numberOfMonths));
    }

    @GetMapping("/rentsPerRenter")
    public ResponseEntity<Object> getRentsPerRenter(@RequestParam(required = false) Integer page){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getRentsPerRenter(page));
    }

    @GetMapping("/bookMoreRented")
    public ResponseEntity<Object> getBooksMoreRented(int numberOfMonths){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardServices.getBooksMoreRented(numberOfMonths));
    }
}
