package com.ilkan.service;

import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import com.ilkan.dto.workdto.WorkDetailResDto;
import com.ilkan.dto.workdto.WorkListResDto;
import com.ilkan.dto.workdto.WorkResDto;
import com.ilkan.repository.WorkRepository;
import com.ilkan.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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

    // 일거리 목록 조회
    public Page<WorkListResDto> getWorkList(Pageable pageable) {
        Page<Work> works = workRepository.findAll(pageable);
        return works.map(WorkListResDto::fromEntity);
    }

    // 일거리 상세 조회
    public WorkDetailResDto getWorkDetail(Long taskId) {
        Work work = workRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("해당 일거리가 존재하지 않습니다."));
        return WorkDetailResDto.fromEntity(work);
    }
}


