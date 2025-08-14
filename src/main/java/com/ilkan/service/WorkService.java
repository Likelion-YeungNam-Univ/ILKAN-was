package com.ilkan.service;

import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import com.ilkan.dto.workdto.WorkResDto;
import com.ilkan.repository.WorkRepository;
import com.ilkan.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;

    // 의뢰자가 등록한 작업 조회
    public Page<WorkResDto> getWorksByRequester(String role, Pageable pageable) {
        Long requesterId = RoleMapper.getUserIdByRole(role);
        Page<Work> works = workRepository.findByRequesterId(requesterId, pageable);
        return works.map(WorkResDto::fromEntity);
    }

    // 수행자가 수행중인 작업 조회
    public Page<WorkResDto> doingWorksByPerformer(String role, Pageable pageable) {
        Long performerId = RoleMapper.getUserIdByRole(role);
        Page<Work> works = workRepository.findByPerformerIdAndStatus(performerId, Status.IN_PROGRESS, pageable);
        return works.map(WorkResDto::fromEntity);
    }

    // 수행자가 지원한 작업 조회
    public Page<WorkResDto> getAppliedWorksByPerformer(String role, Pageable pageable) {
        Long performerId = RoleMapper.getUserIdByRole(role);
        Page<Work> works = workRepository.findByPerformerIdAndStatus(performerId, Status.APPLY_TO, pageable);
        return works.map(WorkResDto::fromEntity);
    }

}


