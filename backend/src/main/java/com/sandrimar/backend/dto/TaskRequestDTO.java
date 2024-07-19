package com.sandrimar.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskRequestDTO (

    @NotBlank(message = "Title is mandatory")
    String title,
    @NotBlank(message = "Description is mandatory")
    String description
) {
}
