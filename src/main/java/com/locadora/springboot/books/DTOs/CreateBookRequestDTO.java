package com.locadora.springboot.books.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateBookRequestDTO(
        @NotBlank(message = "The name cannot be empty") String name,
        @NotBlank(message = "The author cannot be empty") String author,
        @NotNull(message = "The launch date cannot be empty") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate launchDate,
        @NotNull(message = "The total quantity cannot be empty") int totalQuantity,
        @NotNull(message = "The publisher id cannot be empty") int publisherId) {
}
