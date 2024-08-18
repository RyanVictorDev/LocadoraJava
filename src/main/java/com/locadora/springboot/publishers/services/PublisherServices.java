package com.locadora.springboot.publishers.services;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.publishers.repositories.PublisherRepository;
import com.locadora.springboot.users.models.UserModel;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherServices {
    @Autowired
    PublisherRepository publisherRepository;

    public ResponseEntity<Void> create(@Valid CreatePublisherRequestDTO data) {
        if (publisherRepository.findByName(data.name()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        PublisherModel newPublisher = new PublisherModel(data.name(), data.email(), data.telephone(), data.site());
        publisherRepository.save(newPublisher);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<PublisherModel> findAll() {
        List<PublisherModel> publishers = publisherRepository.findAll();
        if (publishers.isEmpty()) throw new ModelNotFoundException();
        return publishers;
    }

    public Optional<PublisherModel> findById(int id){
        return publisherRepository.findById(id);
    }

    public ResponseEntity<Object> update(int id, @Valid CreatePublisherRequestDTO createPublisherRequestDTO){
        Optional<PublisherModel> response = publisherRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publisher not found");

        var publisherModel = response.get();
        BeanUtils.copyProperties(createPublisherRequestDTO, publisherModel);

        return ResponseEntity.status(HttpStatus.OK).body(publisherRepository.save(publisherModel));
    }

    public ResponseEntity<Object> delete(int id){
        Optional<PublisherModel> response = publisherRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publisher not found");

        publisherRepository.delete(response.get());
        return ResponseEntity.status(HttpStatus.OK).body("Publisher deleted succesfully");
    }
}
