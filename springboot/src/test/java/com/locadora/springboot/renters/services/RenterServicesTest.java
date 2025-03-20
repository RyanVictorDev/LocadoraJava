package com.locadora.springboot.renters.services;

import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.renters.DTOs.CreateRenterRequestDTO;
import com.locadora.springboot.renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.renters.validations.RenterValidation;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenterServicesTest {
    
    @Mock
    RenterRepository renterRepository;

    @Mock
    RenterValidation renterValidation;

    @InjectMocks
    RenterServices renterServices;

    private CreateRenterRequestDTO createRenterRequestDTO;
    private RenterModel renterModel;

    @BeforeEach
    void setUp(){
        createRenterRequestDTO = new CreateRenterRequestDTO("Renter", "renter@gmail.com", "(85)987890654", "Endereco", "09354369367");
        renterModel = new RenterModel("Renter", "renter@gmail.com", "(85)987890654", "Endereco", "09354369367");
        renterModel.setId(1);
    }

    @Test
    void shouldCreate(){
        when(renterRepository.save(any(RenterModel.class))).thenAnswer(invocation -> {
            RenterModel renter = invocation.getArgument(0);
            renter.setId(1);
            return renter;
        });

        ResponseEntity<Void> response = renterServices.create(createRenterRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(renterValidation, times(1)).create(createRenterRequestDTO);

        ArgumentCaptor<RenterModel> renterCaptor = ArgumentCaptor.forClass(RenterModel.class);
        verify(renterRepository, times(1)).save(renterCaptor.capture());

        RenterModel savedRenter = renterCaptor.getValue();
        assertEquals(createRenterRequestDTO.name(), savedRenter.getName());
        assertEquals(createRenterRequestDTO.email(), savedRenter.getEmail());
        assertEquals(createRenterRequestDTO.telephone(), savedRenter.getTelephone());
        assertEquals(createRenterRequestDTO.address(), savedRenter.getAddress());
        assertEquals(createRenterRequestDTO.cpf(), savedRenter.getCpf());
    }

    @Test
    void shouldFindById(){
        when(renterRepository.findById(renterModel.getId())).thenReturn(Optional.ofNullable(renterModel));

        Optional<RenterModel> renterResult = renterRepository.findById(renterModel.getId());

        assertTrue(renterResult.isPresent());
        assertEquals("Renter", renterResult.get().getName());
        verify(renterRepository, times(1)).findById(renterModel.getId());
    }

    @Test
    void shouldUpdate(){
        UpdateRenterRequestDTO renterUpdated = new UpdateRenterRequestDTO("Renter updated", "renter@gmail.com", "(85)987890654", "Endereco", "09354369367");

        when(renterRepository.save(any(RenterModel.class))).thenReturn(renterModel);
        when(renterRepository.findById(renterModel.getId())).thenReturn(Optional.ofNullable(renterModel));

        ResponseEntity<Object> response = renterServices.update(renterModel.getId(), renterUpdated);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(renterValidation, times(1)).update(renterUpdated, renterModel.getId());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentRenter(){
        UpdateRenterRequestDTO renterUpdated = new UpdateRenterRequestDTO("Renter updated", "renter@gmail.com", "(85)987890654", "Endereco", "09354369367");

        when(renterRepository.findById(renterModel.getId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = renterServices.update(renterModel.getId(), renterUpdated);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldDeleteRenterSuccessfully() {
        when(renterRepository.findById(renterModel.getId())).thenReturn(Optional.of(renterModel));

        ResponseEntity<Object> response = renterServices.delete(renterModel.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Locatário excluído com sucesso.", response.getBody());
        verify(renterRepository, times(1)).findById(renterModel.getId());
        verify(renterRepository, times(1)).save(renterModel);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentRenter() {
        when(renterRepository.findById(renterModel.getId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = renterServices.delete(renterModel.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Locatário não encontrado.", response.getBody());
        verify(renterRepository, times(1)).findById(renterModel.getId());
    }
}