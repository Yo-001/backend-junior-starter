package com.example.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {
    private static final String REGEX = "^[a-zA-z0-9 ]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if (value == null) return true; // @NotBlank já trata nulos
        return value.matches(REGEX);
    }
}
