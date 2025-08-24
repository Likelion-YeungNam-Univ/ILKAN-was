package com.ilkan.domain.work.repository;

import com.ilkan.domain.work.entity.TaskApplication;
import com.ilkan.domain.profile.entity.User;
import com.ilkan.domain.work.entity.Work;
import com.ilkan.domain.work.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskApplicationRepository extends JpaRepository<TaskApplication,Long> {
    Page<TaskApplication> findByPerformerId_IdAndStatus(Long performerId, Status status, Pageable pageable); // 수행자 자신이 지원한 일거리 내역조회

    Optional<TaskApplication> findByTaskIdAndPerformerId(Work taskId, User performerId);

    Optional<TaskApplication> findByIdAndTaskId_Id(Long applyId, Long taskId);

    boolean existsByTaskId_IdAndPerformerId_Id(Long taskId, Long performerId);

    Page<TaskApplication> findByTaskId_Id(Long taskId, Pageable pageable);

}
