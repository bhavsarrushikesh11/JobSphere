package com.jobsphere.job.specification;

import com.jobsphere.job.entity.Job;
import com.jobsphere.job.enums.JobStatus;
import com.jobsphere.job.enums.JobType;
import org.springframework.data.jpa.domain.Specification;
import com.jobsphere.job.dto.JobFilterRequest;

public class JobSpecification {

    public static Specification<Job> hasLocation(String location) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("location"),
                        location
                );
    }

    public static Specification<Job> hasJobType(JobType jobType) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("jobType"),
                        jobType
                );
    }

    public static Specification<Job> hasStatus(JobStatus status) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<Job> hasMinimumSalary(Double minSalary) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("minSalary"),
                        minSalary
                );
    }

    public static Specification<Job> hasMaximumSalary(Double maxSalary) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("maxSalary"),
                        maxSalary
                );
    }

    public static Specification<Job> hasMinimumExperience(
            Integer minExperience) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("minExperience"),
                        minExperience
                );
    }

    public static Specification<Job> hasMaximumExperience(
            Integer maxExperience) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("maxExperience"),
                        maxExperience
                );
    }

    public static Specification<Job> hasKeyword(String keyword) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + keyword.toLowerCase() + "%"
                );
    }

    public static Specification<Job> buildSpecification(
            JobFilterRequest filter) {

        Specification<Job> specification =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction();

        if (filter.getKeyword() != null
                && !filter.getKeyword().isBlank()) {

            specification = specification.and(
                    hasKeyword(filter.getKeyword())
            );
        }

        if (filter.getLocation() != null
                && !filter.getLocation().isBlank()) {

            specification = specification.and(
                    hasLocation(filter.getLocation())
            );
        }

        if (filter.getJobType() != null) {

            specification = specification.and(
                    hasJobType(filter.getJobType())
            );
        }

        if (filter.getStatus() != null) {

            specification = specification.and(
                    hasStatus(filter.getStatus())
            );
        }

        if (filter.getMinSalary() != null) {

            specification = specification.and(
                    hasMinimumSalary(filter.getMinSalary())
            );
        }

        if (filter.getMaxSalary() != null) {

            specification = specification.and(
                    hasMaximumSalary(filter.getMaxSalary())
            );
        }

        if (filter.getMinExperience() != null) {

            specification = specification.and(
                    hasMinimumExperience(
                            filter.getMinExperience()
                    )
            );
        }

        if (filter.getMaxExperience() != null) {

            specification = specification.and(
                    hasMaximumExperience(
                            filter.getMaxExperience()
                    )
            );
        }

        return specification;
    }
}