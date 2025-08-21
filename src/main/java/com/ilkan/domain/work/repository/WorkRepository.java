package com.ilkan.domain.work.repository;

import com.ilkan.domain.work.entity.Work;
import com.ilkan.domain.work.entity.enums.Status;
import com.ilkan.domain.work.entity.enums.WorkCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {
    Page<Work> findByRequesterId(Long requesterId, Pageable pageable); // 내가(의뢰자) 등록한 일거리조회

    Page<Work> findByPerformer_IdAndStatus(Long performerId, Status status, Pageable pageable);// 내가(수행자) 수행중인 일거리 조회

    Page<Work> findAll(Pageable pageable); // 모든 일거리조회

    Optional<Work> findById(Long taskId); // 모집중 일거리 상세조회

    Optional<Work> findByIdAndRequesterId(Long taskId, Long requesterId); // 일거리 수정/삭제 시 권한체크용

    Page<Work> findByWorkCategory(WorkCategory category, Pageable pageable);
}