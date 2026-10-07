package com.jobsphere.application.controller;

import com.jobsphere.application.dto.ApplicationRequest;
import com.jobsphere.application.dto.ApplicationResponse;
import com.jobsphere.application.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ApplicationResponse applyForJob(
            @Valid @RequestBody ApplicationRequest request) {

        return applicationService.applyForJob(request);
    }
}