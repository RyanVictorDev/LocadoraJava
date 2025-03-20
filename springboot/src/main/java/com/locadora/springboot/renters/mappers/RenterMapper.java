package com.locadora.springboot.renters.mappers;

import com.locadora.springboot.renters.DTOs.RenterResponseDTO;
import com.locadora.springboot.renters.models.RenterModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RenterMapper {

    public List<RenterResponseDTO> toRenterResponseList(List<RenterModel> renterList){
        return renterList.stream().map(this::toRenterResponse).toList();
    }

    public RenterResponseDTO toRenterResponse(RenterModel model){
        return RenterResponseDTO
                .builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .telephone(model.getTelephone())
                .address(model.getAddress())
                .cpf(model.getCpf())
                .build();
    }
}
