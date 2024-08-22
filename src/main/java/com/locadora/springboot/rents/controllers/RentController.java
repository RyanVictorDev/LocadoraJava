package com.locadora.springboot.rents.controllers;

import com.locadora.springboot.rents.DTOs.CreateRentRequestDTO;
import com.locadora.springboot.rents.DTOs.RentResponseDTO;
import com.locadora.springboot.rents.mappers.RentMapper;
import com.locadora.springboot.rents.services.RentServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class RentController {

    @Autowired
    RentMapper rentMapper;

    @Autowired
    RentServices rentServices;

    @PostMapping("/rent")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateRentRequestDTO data){
        return rentServices.create(data);
    }

    @GetMapping("/rent")
    public ResponseEntity<List<RentResponseDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(rentMapper.toRentResponseList(rentServices.findAll()));
    }

    @GetMapping("/rent/{id}")
    public ResponseEntity<RentResponseDTO> getById(@PathVariable(value = "id") int id){
        return ResponseEntity.status(HttpStatus.OK).body(rentMapper.toRentResponse(rentServices.findById(id).get()));
    }
}
