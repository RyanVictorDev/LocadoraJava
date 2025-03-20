package com.locadora.springboot.publishers.services;

import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.publishers.repositories.PublisherRepository;
import com.locadora.springboot.publishers.validations.PublisherValidation;
import com.locadora.springboot.renters.models.RenterModel;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PublisherServices {
    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    PublisherValidation publisherValidation;

    public ResponseEntity<Void> create(@Valid CreatePublisherRequestDTO data) {

        publisherValidation.create(data);

        PublisherModel newPublisher = new PublisherModel(data.name(), data.email(), data.telephone(), data.site());
        publisherRepository.save(newPublisher);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public Page<PublisherModel> findAll(String search, int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (Objects.equals(search, "")){
            Page<PublisherModel> publishers = publisherRepository.findAllByIsDeletedFalse(pageable);
            if (publishers.isEmpty()) throw new ModelNotFoundException();
            return publishers;
        } else {
            Page<PublisherModel> publisherSearch = publisherRepository.findAllByName(search, pageable);
            return publisherSearch;
        }
    }

    public List<PublisherModel> findAllWithoutPagination(@RequestParam(required = false) String search) {
        if (Objects.isNull(search) || Objects.equals(search, "")) {
            return publisherRepository.findAllByIsDeletedFalse(Sort.by(Sort.Direction.DESC, "id"));
        } else {
            return publisherRepository.findAllByName(search, Sort.by(Sort.Direction.DESC, "id"));
        }
    }

    public Optional<PublisherModel> findById(int id){
        return publisherRepository.findById(id);
    }

    public ResponseEntity<Object> update(int id, @Valid UpdatePublisherRecordDTO updatePublisherRecordDTO){
        Optional<PublisherModel> response = publisherRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Editora não encontrada.");

        publisherValidation.update(updatePublisherRecordDTO, id);

        var publisherModel = response.get();
        BeanUtils.copyProperties(updatePublisherRecordDTO, publisherModel);

        return ResponseEntity.status(HttpStatus.OK).body(publisherRepository.save(publisherModel));
    }

    public ResponseEntity<Object> delete(int id){
        Optional<PublisherModel> response = publisherRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Editora não encontrada.");

        publisherValidation.validDeletePublisher(id);

        PublisherModel publisher = response.get();

        publisher.setDeleted(true);

        publisherRepository.save(publisher);

        return ResponseEntity.status(HttpStatus.OK).body("Editora excluída com sucesso.");
    }
}