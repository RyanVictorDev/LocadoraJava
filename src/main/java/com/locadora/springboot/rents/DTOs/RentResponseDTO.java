package com.locadora.springboot.rents.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.renters.models.RenterModel;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RentResponseDTO(
        int id,
        RenterModel renter,
        BookModel book,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate deadLine,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate devolutionDate
) {
}
