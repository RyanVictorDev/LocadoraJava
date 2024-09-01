package com.locadora.springboot.dashboard.DTOs;

import lombok.Builder;

@Builder
public record BooksMoreRented(
        String name,
        int totalRents) {
}