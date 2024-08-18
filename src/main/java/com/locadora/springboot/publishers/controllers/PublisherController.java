package com.locadora.springboot.publishers.controllers;

import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.DTOs.PublisherResponseDTO;
import com.locadora.springboot.publishers.mappers.PublisherMapper;
import com.locadora.springboot.publishers.services.PublisherServices;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublisherController {

    @Autowired
    PublisherMapper publisherMapper;

    @Autowired
    PublisherServices publisherServices;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/publisher")
    public ResponseEntity<Void> create(@RequestBody @Valid CreatePublisherRequestDTO data) {
        return publisherServices.create(data);
    }

    @GetMapping("/publisher")
    public ResponseEntity<List<PublisherResponseDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(publisherMapper.toPublisherResponseList(publisherServices.findAll()));
    }
}
