package com.jobsphere.job.controller;

import com.jobsphere.common.dto.PageResponse;
import com.jobsphere.job.dto.JobFilterRequest;
import com.jobsphere.job.dto.JobRequest;
import com.jobsphere.job.dto.JobResponse;
import com.jobsphere.job.service.JobService;

import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public JobResponse createJob(
            @Valid @RequestBody JobRequest request) {

        return jobService.createJob(request);
    }

    @GetMapping
    public PageResponse<JobResponse> getAllJobs(Pageable pageable) {

        return jobService.getAllJobs(pageable);
    }

    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable Long id) {

        return jobService.getJobById(id);
    }

    @PutMapping("/{id}")
    public JobResponse updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request) {

        return jobService.updateJob(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {

        jobService.deleteJob(id);
    }

    @GetMapping("/search")
    public PageResponse<JobResponse> searchJobs(
            @RequestParam String keyword,
            Pageable pageable) {

        return jobService.searchJobs(keyword, pageable);
    }

    @GetMapping("/filter")
    public PageResponse<JobResponse> filterJobs(
            @Valid JobFilterRequest filter,
            Pageable pageable) {

        return jobService.filterJobs(filter, pageable);
    }
}