package com.example.app.dto;

public record ErrorResponse(
        String message,
        String field
) {}
