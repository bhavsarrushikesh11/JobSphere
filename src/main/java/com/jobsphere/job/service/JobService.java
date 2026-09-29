package com.jobsphere.job.service;

import com.jobsphere.company.entity.Company;
import com.jobsphere.company.repository.CompanyRepository;
import com.jobsphere.exception.ResourceNotFoundException;
import com.jobsphere.job.dto.JobRequest;
import com.jobsphere.job.dto.JobResponse;
import com.jobsphere.job.entity.Job;
import com.jobsphere.job.repository.JobRepository;

import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    public JobService(
            JobRepository jobRepository,
            CompanyRepository companyRepository) {

        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
    }

    public JobResponse createJob(JobRequest request) {

        // Find company using company ID
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: "
                                        + request.getCompanyId()
                        )
                );

        // Create Job entity
        Job job = new Job();

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());

        job.setMinExperience(request.getMinExperience());
        job.setMaxExperience(request.getMaxExperience());

        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());

        job.setJobType(request.getJobType());
        job.setStatus(request.getStatus());

        job.setApplicationDeadline(request.getApplicationDeadline());

        // Set company relationship
        job.setCompany(company);

        // Save job
        Job savedJob = jobRepository.save(job);

        // Convert Entity → Response DTO
        return mapToResponse(savedJob);
    }

    private JobResponse mapToResponse(Job job) {

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),

                job.getMinExperience(),
                job.getMaxExperience(),

                job.getMinSalary(),
                job.getMaxSalary(),

                job.getJobType(),
                job.getStatus(),

                job.getApplicationDeadline(),

                job.getCompany().getId(),
                job.getCompany().getName(),

                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }
}