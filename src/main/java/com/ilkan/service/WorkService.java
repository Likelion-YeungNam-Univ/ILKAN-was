package com.ilkan.service;

import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import com.ilkan.dto.workdto.WorkResponseDto;
import com.ilkan.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;

    // 의뢰자로서 등록한 일거리 조회 (DTO 반환)
    public Page<WorkResponseDto> getWorksByRequester(Long requesterId, Pageable pageable) {
        Page<Work> works = workRepository.findByRequesterId(requesterId, pageable);
        return works.map(WorkResponseDto::fromEntity);
    }

    // 수행자로서 수행중인 일거리 조회 (DTO 반환)
    public Page<WorkResponseDto> doingWorksByPerformer(Long performerId, Pageable pageable) {
        Page<Work> works = workRepository.findByPerformerIdAndStatus(performerId, Status.IN_PROGRESS, pageable);
        return works.map(WorkResponseDto::fromEntity);
    }
}

