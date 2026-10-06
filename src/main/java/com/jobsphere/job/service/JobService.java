package com.jobsphere.job.service;

import com.jobsphere.company.entity.Company;
import com.jobsphere.company.repository.CompanyRepository;
import com.jobsphere.exception.ForbiddenException;
import com.jobsphere.exception.ResourceNotFoundException;
import com.jobsphere.job.dto.JobFilterRequest;
import com.jobsphere.job.dto.JobRequest;
import com.jobsphere.job.dto.JobResponse;
import com.jobsphere.job.entity.Job;
import com.jobsphere.job.repository.JobRepository;
import com.jobsphere.common.dto.PageResponse;

import com.jobsphere.job.specification.JobSpecification;
import com.jobsphere.user.enums.UserRole;
import com.jobsphere.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jobsphere.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public JobService(
            JobRepository jobRepository,
            CompanyRepository companyRepository,
            UserRepository userRepository) {

        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public JobResponse createJob(JobRequest request) {

        // 1. Find the company
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: " + request.getCompanyId()
                        )
                );

        // 2. Get the currently logged-in user from SecurityContext
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // 3. Find that user in the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );

        // 4. Create Job object
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

        // 5. Set company
        job.setCompany(company);

        // 6. Set the logged-in user as job creator
        job.setCreatedBy(user);

        // 7. Save job
        Job savedJob = jobRepository.save(job);

        // 8. Convert to response
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

        User currentUser = getCurrentUser();

        checkJobOwnership(job, currentUser);

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

        User currentUser = getCurrentUser();

        checkJobOwnership(job, currentUser);

        jobRepository.delete(job);
    }

    public PageResponse<JobResponse> searchJobs(
            String keyword,
            Pageable pageable) {

        Page<Job> jobPage =
                jobRepository.findByTitleContainingIgnoreCase(
                        keyword,
                        pageable
                );

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

    public PageResponse<JobResponse> filterJobs(
            JobFilterRequest filter,
            Pageable pageable) {

        Specification<Job> specification =
                JobSpecification.buildSpecification(filter);

        Page<Job> jobPage =
                jobRepository.findAll(specification, pageable);

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

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }

    private void checkJobOwnership(Job job, User currentUser) {

        // ADMIN can access any job
        if (currentUser.getRole() == UserRole.ADMIN) {
            return;
        }

        // Job has no owner
        if (job.getCreatedBy() == null) {
            throw new ForbiddenException(
                    "You are not allowed to modify this job"
            );
        }

        // Job belongs to another user
        if (!job.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "You are not allowed to modify this job"
            );
        }
    }
}