package com.jobsphere.job.dto;

import com.jobsphere.job.enums.JobStatus;
import com.jobsphere.job.enums.JobType;
import com.jobsphere.validation.ValidJobFilterRequest;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@ValidJobFilterRequest
public class JobFilterRequest {

    @Size(max = 100, message = "Keyword cannot exceed 100 characters")
    private String keyword;
    private String location;
    private JobType jobType;
    private JobStatus status;

    @PositiveOrZero(message = "Minimum salary cannot be negative")
    private Double minSalary;

    @PositiveOrZero(message = "Maximum salary cannot be negative")
    private Double maxSalary;

    @PositiveOrZero(message = "Minimum experience cannot be negative")
    private Integer minExperience;

    @PositiveOrZero(message = "Maximum experience cannot be negative")
    private Integer maxExperience;

    private LocalDate deadlineBefore;

    private LocalDate deadlineAfter;

    public JobFilterRequest() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Integer getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(Integer minExperience) {
        this.minExperience = minExperience;
    }

    public Integer getMaxExperience() {
        return maxExperience;
    }

    public void setMaxExperience(Integer maxExperience) {
        this.maxExperience = maxExperience;
    }

    public LocalDate getDeadlineBefore() {
        return deadlineBefore;
    }

    public void setDeadlineBefore(LocalDate deadlineBefore) {
        this.deadlineBefore = deadlineBefore;
    }

    public LocalDate getDeadlineAfter() {
        return deadlineAfter;
    }

    public void setDeadlineAfter(LocalDate deadlineAfter) {
        this.deadlineAfter = deadlineAfter;
    }
}