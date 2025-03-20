package com.locadora.springboot.renters.services;

import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.renters.DTOs.CreateRenterRequestDTO;
import com.locadora.springboot.renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.renters.validations.RenterValidation;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RenterServices {

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private RenterValidation renterValidation;

    public ResponseEntity<Void> create(@Valid CreateRenterRequestDTO data){
        renterValidation.create(data);

        RenterModel newRenter = new RenterModel(data.name(), data.email(), data.telephone(), data.address(), data.cpf());
        renterRepository.save(newRenter);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public Page<RenterModel> findAll(String search, int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (Objects.equals(search, "")){
            Page<RenterModel> renters = renterRepository.findAllByIsDeletedFalse(pageable);
            if (renters.isEmpty()) throw new ModelNotFoundException();
            return renters;
        } else {
            Page<RenterModel> renterSearch = renterRepository.findAllByName(search, pageable);
            return renterSearch;
        }
    }

    public List<RenterModel> findAllWithoutPagination(String search) {
        if (Objects.isNull(search) || Objects.equals(search, "")) {
            return renterRepository.findAllByIsDeletedFalse(Sort.by(Sort.Direction.DESC, "id"));
        } else {
            return renterRepository.findAllByName(search, Sort.by(Sort.Direction.DESC, "id"));
        }
    }

    public Optional<RenterModel> findById(int id) {
        return renterRepository.findById(id);
    }

    public ResponseEntity<Object> update(int id, @Valid UpdateRenterRequestDTO updateRenterRequestDTO){
        Optional<RenterModel> response = renterRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Locatário não encontrado.");

        renterValidation.update(updateRenterRequestDTO, id);

        RenterModel renterModel = response.get();
        BeanUtils.copyProperties(updateRenterRequestDTO, renterModel);

        return ResponseEntity.status(HttpStatus.OK).body(renterRepository.save(renterModel));
    }

    public ResponseEntity<Object> delete(int id){
        Optional<RenterModel> response = renterRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Locatário não encontrado.");

        renterValidation.validateDeleteRenter(id);

        RenterModel renter = response.get();

        renter.setDeleted(true);

        renterRepository.save(renter);

        return ResponseEntity.status(HttpStatus.OK).body("Locatário excluído com sucesso.");
    }
}
