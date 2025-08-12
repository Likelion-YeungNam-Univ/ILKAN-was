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

    // role 기반 의뢰자가 등록한 작업 조회
    public Page<WorkResponseDto> getWorksByRequester(String role, Pageable pageable) {
        Long requesterId = getUserIdByRole(role);
        Page<Work> works = workRepository.findByRequesterId(requesterId, pageable);
        return works.map(WorkResponseDto::fromEntity);
    }

    // role 기반 수행자가 수행중인 작업 조회
    public Page<WorkResponseDto> doingWorksByPerformer(String role, Pageable pageable) {
        Long performerId = getUserIdByRole(role);
        Page<Work> works = workRepository.findByPerformerIdAndStatus(performerId, Status.IN_PROGRESS, pageable);
        return works.map(WorkResponseDto::fromEntity);
    }

    // role → userId 매핑
    private Long getUserIdByRole(String role) {
        switch (role.toUpperCase()) {
            case "REQUESTER": return 1L;
            case "PERFORMER": return 2L;
            case "OWNER": return 3L;
            default: throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}


