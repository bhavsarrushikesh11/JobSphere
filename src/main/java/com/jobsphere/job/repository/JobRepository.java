package com.jobsphere.job.repository;

import com.jobsphere.job.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobRepository
        extends JpaRepository<Job, Long>,
        JpaSpecificationExecutor<Job> {

    Page<Job> findByTitleContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );
}