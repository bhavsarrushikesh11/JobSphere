package com.jobsphere.job.service;

import com.jobsphere.company.entity.Company;
import com.jobsphere.company.repository.CompanyRepository;
import com.jobsphere.exception.ResourceNotFoundException;
import com.jobsphere.job.dto.JobRequest;
import com.jobsphere.job.dto.JobResponse;
import com.jobsphere.job.entity.Job;
import com.jobsphere.job.repository.JobRepository;
import com.jobsphere.common.dto.PageResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;

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

    public PageResponse<JobResponse> getAllJobs(Pageable pageable) {

        Page<Job> jobPage = jobRepository.findAll(pageable);

        List<JobResponse> jobs = jobPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new PageResponse<>(
                jobs,
                jobPage.getNumber(),
                jobPage.getSize(),
                jobPage.getTotalElements(),
                jobPage.getTotalPages(),
                jobPage.isFirst(),
                jobPage.isLast()
        );
    }

    public JobResponse getJobById(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id: " + id
                        )
                );

        return mapToResponse(job);
    }

    public JobResponse updateJob(Long id, JobRequest request) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id: " + id
                        )
                );

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: "
                                        + request.getCompanyId()
                        )
                );

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

        job.setCompany(company);

        Job updatedJob = jobRepository.save(job);

        return mapToResponse(updatedJob);
    }

    public void deleteJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id: " + id
                        )
                );

        jobRepository.delete(job);
    }
}