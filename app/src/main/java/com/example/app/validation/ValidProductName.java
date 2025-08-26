package com.example.app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductName {
    String message() default "Product name contains invalid characters.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
