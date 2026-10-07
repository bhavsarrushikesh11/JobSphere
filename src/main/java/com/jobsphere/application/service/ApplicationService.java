package com.jobsphere.application.service;

import com.jobsphere.application.dto.ApplicationRequest;
import com.jobsphere.application.dto.ApplicationResponse;
import com.jobsphere.application.entity.Application;
import com.jobsphere.application.enums.ApplicationStatus;
import com.jobsphere.application.repository.ApplicationRepository;
import com.jobsphere.exception.BusinessRuleException;
import com.jobsphere.exception.ResourceAlreadyExistsException;
import com.jobsphere.exception.ResourceNotFoundException;
import com.jobsphere.job.entity.Job;
import com.jobsphere.job.repository.JobRepository;
import com.jobsphere.user.entity.User;
import com.jobsphere.user.repository.UserRepository;
import com.jobsphere.user.enums.UserRole;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            JobRepository jobRepository,
            UserRepository userRepository) {

        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public ApplicationResponse applyForJob(ApplicationRequest request) {

        // 1. Get currently logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );

        // 2. Make sure the user is a JOB_SEEKER
        if (user.getRole() != UserRole.JOB_SEEKER) {
            throw new BusinessRuleException(
                    "Only job seekers can apply for jobs"
            );
        }

        // 3. Find the job
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id: " + request.getJobId()
                        )
                );

        // 4. Check job status
        if (job.getStatus() != com.jobsphere.job.enums.JobStatus.OPEN) {
            throw new BusinessRuleException(
                    "You can only apply for open jobs"
            );
        }

        // 5. Check application deadline
        if (job.getApplicationDeadline() != null
                && job.getApplicationDeadline()
                .isBefore(LocalDate.now())) {

            throw new BusinessRuleException(
                    "Application deadline has passed"
            );
        }

        // 6. Prevent duplicate application
        if (applicationRepository.existsByUserIdAndJobId(
                user.getId(),
                job.getId())) {

            throw new ResourceAlreadyExistsException(
                    "You have already applied for this job"
            );
        }

        // 7. Create application
        Application application = new Application();

        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setCoverLetter(request.getCoverLetter());

        // 8. Save application
        Application savedApplication =
                applicationRepository.save(application);

        // 9. Convert to response
        return mapToResponse(savedApplication);
    }

    private ApplicationResponse mapToResponse(
            Application application) {

        ApplicationResponse response =
                new ApplicationResponse();

        response.setId(application.getId());

        response.setJobId(
                application.getJob().getId()
        );

        response.setJobTitle(
                application.getJob().getTitle()
        );

        response.setUserId(
                application.getUser().getId()
        );

        response.setUserName(
                application.getUser().getFirstName()
                        + " "
                        + application.getUser().getLastName()
        );

        response.setUserEmail(
                application.getUser().getEmail()
        );

        response.setStatus(
                application.getStatus()
        );

        response.setCoverLetter(
                application.getCoverLetter()
        );

        response.setAppliedAt(
                application.getAppliedAt()
        );

        response.setUpdatedAt(
                application.getUpdatedAt()
        );

        return response;
    }
}