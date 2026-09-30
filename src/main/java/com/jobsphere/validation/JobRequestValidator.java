package com.jobsphere.validation;

import com.jobsphere.job.dto.JobRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class JobRequestValidator
        implements ConstraintValidator<ValidJobRequest, JobRequest> {

    @Override
    public boolean isValid(
            JobRequest request,
            ConstraintValidatorContext context) {

        if (request == null) {
            return true;
        }

        boolean valid = true;

        if (request.getMinExperience() != null
                && request.getMaxExperience() != null
                && request.getMinExperience() > request.getMaxExperience()) {

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "Minimum experience cannot be greater than maximum experience"
                    )
                    .addPropertyNode("minExperience")
                    .addConstraintViolation();

            valid = false;
        }

        if (request.getMinSalary() != null
                && request.getMaxSalary() != null
                && request.getMinSalary() > request.getMaxSalary()) {

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "Minimum salary cannot be greater than maximum salary"
                    )
                    .addPropertyNode("minSalary")
                    .addConstraintViolation();

            valid = false;
        }

        return valid;
    }
}