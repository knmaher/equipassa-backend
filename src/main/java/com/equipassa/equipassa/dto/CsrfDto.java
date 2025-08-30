package com.equipassa.equipassa.dto;

public record CsrfDto(String headerName, String parameterName, String token) {
}
