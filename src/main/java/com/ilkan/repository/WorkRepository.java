package com.ilkan.repository;

import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Long> {
    Page<Work> findByRequesterId(Long requesterId, Pageable pageable); // 내가 등록한 일거리조회

    Page<Work> findByPerformerId(Long performerId, Status status, Pageable pageable);// 내가 수행중인 일거리 조회
}