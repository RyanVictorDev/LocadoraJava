package com.locadora.springboot.books.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.locadora.springboot.publishers.models.PublisherModel;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BookResponseDTO(
        int id,
        String name,
        String author,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate launchDate,
        int totalQuantity,
        int totalInUse,
        PublisherModel publisher) {
}
