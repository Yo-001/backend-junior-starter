package com.example.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest (
        @NotBlank(message = "{product.name.notblank")
        @Size(max = 120, message = "{product.name.size") String name,
        @NotNull(message = "{product.price.notnull")
        @DecimalMin(value = "0.01", message = "{product.price.min") BigDecimal price
) {}

