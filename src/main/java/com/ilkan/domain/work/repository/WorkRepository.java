package com.ilkan.domain.work.repository;

import com.ilkan.domain.work.entity.Work;
import com.ilkan.domain.work.entity.enums.Status;
import com.ilkan.domain.work.entity.enums.WorkCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {
    Page<Work> findByRequesterIdAndStatusIn(Long requesterId, List<Status> statuses, Pageable pageable);// 내가(의뢰자) 등록한 일거리조회

    Page<Work> findByPerformer_IdAndStatusIn(Long performerId, Collection<Status> statuses, Pageable pageable); // 수행자 동시버튼
    Page<Work> findByRequester_IdAndStatusIn(Long requesterId, Collection<Status> statuses, Pageable pageable); // 의뢰자 동시버튼

    Page<Work> findAll(Pageable pageable); // 모든 일거리조회

    Optional<Work> findById(Long taskId); // 모집중 일거리 상세조회

    Optional<Work> findByIdAndRequesterId(Long taskId, Long requesterId); // 일거리 수정/삭제 시 권한체크용

    Page<Work> findByStatusIn(List<Status> statuses, Pageable pageable);

    Page<Work> findByWorkCategoryAndStatusIn(WorkCategory category, List<Status> statuses, Pageable pageable);

    List<Work> findByTaskStartBeforeAndStatus(LocalDateTime now, Status status);

    // 지금 시간 이후인데 종료 안된 Work
    List<Work> findByTaskEndBeforeAndStatus(LocalDateTime now, Status status);
}
