package com.jobsphere.job.controller;

import com.jobsphere.job.dto.JobRequest;
import com.jobsphere.job.dto.JobResponse;
import com.jobsphere.job.service.JobService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

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
}