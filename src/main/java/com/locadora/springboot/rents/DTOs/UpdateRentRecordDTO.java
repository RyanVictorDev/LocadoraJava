package com.locadora.springboot.rents.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateRentRecordDTO(
        @NotNull int renterId,
        @NotNull int bookId,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate deadLine,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate devolutionDate
) {
}