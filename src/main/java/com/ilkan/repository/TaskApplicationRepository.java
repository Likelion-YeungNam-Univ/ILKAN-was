package com.ilkan.repository;

import com.ilkan.domain.entity.TaskApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskApplicationRepository extends JpaRepository<TaskApplication,Long> {
    Page<TaskApplication> findByPerformer_Id(Long performerId, Pageable pageable);
}
