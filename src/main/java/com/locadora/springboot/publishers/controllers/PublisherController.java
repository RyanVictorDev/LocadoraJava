package com.locadora.springboot.publishers.controllers;

import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.DTOs.PublisherResponseDTO;
import com.locadora.springboot.publishers.mappers.PublisherMapper;
import com.locadora.springboot.publishers.services.PublisherServices;
import com.locadora.springboot.renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.repositories.UserRepository;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.ObjectName;
import java.util.List;

@RestController
public class PublisherController {

    @Autowired
    PublisherMapper publisherMapper;

    @Autowired
    PublisherServices publisherServices;

    @PostMapping("/publisher")
    public ResponseEntity<Void> create(@RequestBody @Valid CreatePublisherRequestDTO data) {
        return publisherServices.create(data);
    }

    @GetMapping("/publisher")
    public ResponseEntity<List<PublisherResponseDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(publisherMapper.toPublisherResponseList(publisherServices.findAll()));
    }

    @GetMapping("/publisher/{id}")
    public ResponseEntity<PublisherResponseDTO> getById(@PathVariable(value = "id") int id){
        return ResponseEntity.status(HttpStatus.OK).body(publisherMapper.toPublisherResponse(publisherServices.findById(id).get()));
    }

    @PutMapping("/publisher/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") int id, @RequestBody @Valid UpdateRenterRequestDTO updateRenterRequestDTO){
        return publisherServices.update(id, updateRenterRequestDTO);
    }

    @DeleteMapping("/publisher/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") int id){
        return publisherServices.delete(id);
    }
}