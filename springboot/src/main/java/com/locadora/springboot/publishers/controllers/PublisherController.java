package com.locadora.springboot.publishers.controllers;

import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.DTOs.PublisherResponseDTO;
import com.locadora.springboot.publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.springboot.publishers.mappers.PublisherMapper;
import com.locadora.springboot.publishers.services.PublisherServices;
import com.locadora.springboot.renters.DTOs.UpdateRenterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> getAll(@RequestParam(required = false) String search, @RequestParam(required = false) Integer page){
        if (page == null) {
            return ResponseEntity.status(HttpStatus.OK).body(publisherMapper.toPublisherResponseList(publisherServices.findAllWithoutPagination(search)));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(publisherServices.findAll(search, page).map(publisherMapper::toPublisherResponse));
        }
    }

    @GetMapping("/publisher/{id}")
    public ResponseEntity<PublisherResponseDTO> getById(@PathVariable(value = "id") int id){
        return ResponseEntity.status(HttpStatus.OK).body(publisherMapper.toPublisherResponse(publisherServices.findById(id).get()));
    }

    @PutMapping("/publisher/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") int id, @RequestBody @Valid UpdatePublisherRecordDTO updatePublisherRecordDTO){
        return publisherServices.update(id, updatePublisherRecordDTO);
    }

    @DeleteMapping("/publisher/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") int id){
        return publisherServices.delete(id);
    }
}