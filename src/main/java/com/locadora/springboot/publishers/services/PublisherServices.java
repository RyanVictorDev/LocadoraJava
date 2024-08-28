package com.locadora.springboot.publishers.services;

import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.publishers.repositories.PublisherRepository;
import com.locadora.springboot.publishers.validations.PublisherValidation;
import com.locadora.springboot.renters.DTOs.UpdateRenterRequestDTO;
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

    @Autowired
    PublisherValidation publisherValidation;

    public ResponseEntity<Void> create(@Valid CreatePublisherRequestDTO data) {

        publisherValidation.validName(data);
        publisherValidation.validEmail(data);
        publisherValidation.validTelephone(data);
        publisherValidation.validSite(data);

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

    public ResponseEntity<Object> update(int id, @Valid UpdatePublisherRecordDTO updatePublisherRecordDTO){
        Optional<PublisherModel> response = publisherRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publisher not found");

        publisherValidation.validNameUpdate(updatePublisherRecordDTO);
        publisherValidation.validEmailUpdate(updatePublisherRecordDTO);
        publisherValidation.validTelephoneUpdate(updatePublisherRecordDTO);
        publisherValidation.validSiteUpdate(updatePublisherRecordDTO);

        var publisherModel = response.get();
        BeanUtils.copyProperties(updatePublisherRecordDTO, publisherModel);

        return ResponseEntity.status(HttpStatus.OK).body(publisherRepository.save(publisherModel));
    }

    public ResponseEntity<Object> delete(int id){
        Optional<PublisherModel> response = publisherRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publisher not found");

        publisherValidation.validDeletePublisher(id);

        publisherRepository.delete(response.get());
        return ResponseEntity.status(HttpStatus.OK).body("Publisher deleted succesfully");
    }
}
