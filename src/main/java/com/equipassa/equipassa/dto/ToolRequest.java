package com.equipassa.equipassa.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ToolRequest(
        @NotBlank String name,
        String description,
        @NotBlank String category,
        @NotBlank String conditionStatus,
        @NotNull @Min(0) Integer quantityAvailable
) {
}
