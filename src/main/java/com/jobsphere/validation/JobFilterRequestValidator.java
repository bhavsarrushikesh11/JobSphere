package com.jobsphere.validation;

import com.jobsphere.job.dto.JobFilterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class JobFilterRequestValidator
        implements ConstraintValidator<ValidJobFilterRequest, JobFilterRequest> {

    @Override
    public boolean isValid(
            JobFilterRequest filter,
            ConstraintValidatorContext context) {

        if (filter == null) {
            return true;
        }

        boolean valid = true;

        if (filter.getMinSalary() != null
                && filter.getMaxSalary() != null
                && filter.getMinSalary() > filter.getMaxSalary()) {

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "Minimum salary cannot be greater than maximum salary"
                    )
                    .addPropertyNode("minSalary")
                    .addConstraintViolation();

            valid = false;
        }

        if (filter.getMinExperience() != null
                && filter.getMaxExperience() != null
                && filter.getMinExperience() > filter.getMaxExperience()) {

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "Minimum experience cannot be greater than maximum experience"
                    )
                    .addPropertyNode("minExperience")
                    .addConstraintViolation();

            valid = false;
        }

        return valid;
    }
}