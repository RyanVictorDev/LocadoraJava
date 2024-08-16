package com.locadora.springboot.publishers.controllers;

import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.mappers.PublisherMapper;
import com.locadora.springboot.publishers.services.PublisherServices;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
