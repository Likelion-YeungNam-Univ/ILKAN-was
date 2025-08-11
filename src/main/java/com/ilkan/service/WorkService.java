package com.ilkan.service;

import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import com.ilkan.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

// WorkService.java
@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;

    // createdAt 기준 내림차순 등록한 작업 조회 ( 의뢰자 기준 )
    public Page<Work> getWorksByRequester(Long requesterId, Pageable pageable) {
        // pageable에 sort 기본값이 없는 경우를 대비해서 정렬 보정
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by("createdAt").descending());
        }

        return workRepository.findByRequesterId(requesterId, pageable);
    }

    // createdAt 기준 내림차순 수행중인 작업 조회 ( 수행자 기준 )
    public Page<Work> doingWorksByPerformer(Long performerId, Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by("createdAt").descending());
        }

        return workRepository.findByPerformerIdAndStatus(performerId, Status.IN_PROGRESS, pageable);
    }

}

