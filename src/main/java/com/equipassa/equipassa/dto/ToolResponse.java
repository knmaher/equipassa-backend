package com.equipassa.equipassa.dto;

import java.util.List;

public record ToolResponse(
        Long id,
        String name,
        String description,
        String category,
        String conditionStatus,
        Integer quantityAvailable,
        List<String> imageUrls
) {
}
