package com.ilkan.repository;

import com.ilkan.domain.entity.TaskApplication;
import com.ilkan.domain.entity.User;
import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskApplicationRepository extends JpaRepository<TaskApplication,Long> {
    Page<TaskApplication> findByPerformerId_IdAndStatus(Long performerId, Status status, Pageable pageable); // 수행자 자신이 지원한 일거리 내역조회

    Page<TaskApplication> findByTaskId_Requester_IdAndStatus(Long requesterId, Status status, Pageable pageable); // 의뢰자 기준 해당 일거리 지원서목록 조회

    Optional<TaskApplication> findByTaskIdAndPerformerId(Work taskId, User performerId);
}
