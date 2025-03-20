package com.locadora.springboot.publishers.mappers;

import com.locadora.springboot.publishers.DTOs.PublisherResponseDTO;
import com.locadora.springboot.publishers.models.PublisherModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublisherMapper {

    public List<PublisherResponseDTO> toPublisherResponseList(List<PublisherModel> publisherList){
        return publisherList.stream().map(this::toPublisherResponse).toList();
    }

    public PublisherResponseDTO toPublisherResponse(PublisherModel model){
        return PublisherResponseDTO
                .builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .telephone(model.getTelephone())
                .site(model.getSite())
                .build();
    }
}
