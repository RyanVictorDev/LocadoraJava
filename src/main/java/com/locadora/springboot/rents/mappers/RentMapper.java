package com.locadora.springboot.rents.mappers;

import com.locadora.springboot.rents.DTOs.RentResponseDTO;
import com.locadora.springboot.rents.models.RentModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RentMapper {

    public List<RentResponseDTO> toRentResponseList(List<RentModel> rentList){
        return rentList.stream().map(this::toRentResponse).toList();
    }

    public RentResponseDTO toRentResponse(RentModel model){
        return RentResponseDTO
                .builder()
                .id(model.getId())
                .book(model.getBook())
                .renter(model.getRenter())
                .deadLine(model.getDeadLine())
                .devolutionDate(model.getRentDate())
                .rentDate(model.getRentDate())
                .status(model.getStatus())
                .build();
    }
}
