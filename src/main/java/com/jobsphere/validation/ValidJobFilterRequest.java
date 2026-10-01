package com.jobsphere.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JobFilterRequestValidator.class)
@Documented
public @interface ValidJobFilterRequest {

    String message() default "Invalid job filter details";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}