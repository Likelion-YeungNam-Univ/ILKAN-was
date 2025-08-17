package com.ilkan.repository;

import com.ilkan.domain.entity.TaskApplication;
import com.ilkan.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskApplicationRepository extends JpaRepository<TaskApplication,Long> {
    Page<TaskApplication> findByPerformerId_IdAndStatus(Long performerId, Status status, Pageable pageable);
}
