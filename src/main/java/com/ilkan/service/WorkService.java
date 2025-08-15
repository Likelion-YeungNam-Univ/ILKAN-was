package com.ilkan.service;

import com.ilkan.domain.entity.User;
import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import com.ilkan.dto.workdto.WorkDetailResDto;
import com.ilkan.dto.workdto.WorkListResDto;
import com.ilkan.dto.workdto.WorkReqDto;
import com.ilkan.dto.workdto.WorkResDto;
import com.ilkan.exception.UserWorkExceptions;
import com.ilkan.repository.UserRepository;
import com.ilkan.repository.WorkRepository;
import com.ilkan.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;
    private final UserRepository userRepository;

    // 의뢰자가 등록한 작업 조회
    @Transactional(readOnly = true)
    public Page<WorkResDto> getWorksByRequester(String role, Pageable pageable) {
        if (!"REQUESTER".equals(role)) {
            throw new UserWorkExceptions.RequesterForbidden();
        }
        Long requesterId = RoleMapper.getUserIdByRole(role);
        Page<Work> works = workRepository.findByRequesterId(requesterId, pageable);

        if (works.isEmpty()) {
            throw new UserWorkExceptions.NoUploadedWorks(); // 등록한 일거리 없음 예외
        }

        return works.map(WorkResDto::fromEntity);
    }

    // 수행자가 수행중인 작업 조회
    @Transactional(readOnly = true)
    public Page<WorkResDto> doingWorksByPerformer(String role, Pageable pageable) {
        if (!"PERFORMER".equals(role)) {
            throw new UserWorkExceptions.PerformerForbidden();
        }
        Long performerId = RoleMapper.getUserIdByRole(role);
        Page<Work> works = workRepository.findByPerformerIdAndStatus(performerId, Status.IN_PROGRESS, pageable);

        if (works.isEmpty()) {
            throw new UserWorkExceptions.NoDoingWorks(); // 수행중인 일거리 없음 예외
        }

        return works.map(WorkResDto::fromEntity);
    }

    // 수행자가 지원한 작업 조회
    @Transactional(readOnly = true)
    public Page<WorkResDto> getAppliedWorksByPerformer(String role, Pageable pageable) {
        if (!"PERFORMER".equals(role)) {
            throw new UserWorkExceptions.PerformerForbidden();
        }
        Long performerId = RoleMapper.getUserIdByRole(role);
        Page<Work> works = workRepository.findByPerformerIdAndStatus(performerId, Status.APPLY_TO, pageable);

        if (works.isEmpty()) {
            throw new UserWorkExceptions.NoAppliedWorks(); // 지원한 일거리 없음 예외
        }

        return works.map(WorkResDto::fromEntity);
    }

    // 일거리 목록 조회
    @Transactional(readOnly = true)
    public Page<WorkListResDto> getWorkList(Pageable pageable) {
        Page<Work> works = workRepository.findAll(pageable);
        if (works.isEmpty()) {
            throw new UserWorkExceptions.WorkNotFound(); // 전체 일거리 없음 예외
        }
        return works.map(WorkListResDto::fromEntity);
    }

    // 일거리 상세 조회 (예외처리 적용)
    @Transactional(readOnly = true)
    public WorkDetailResDto getWorkDetail(Long taskId) {
        Work work = workRepository.findById(taskId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);
        return WorkDetailResDto.fromEntity(work);
    }

    // 일거리 등록
    @Transactional
    public Work createWork(Long requesterId, WorkReqDto dto) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(UserWorkExceptions.RequesterForbidden::new);
        Work work = dto.toEntity(requester);
        return workRepository.save(work);
    }

    // 일거리 수정
    @Transactional
    public Work updateWork(Long taskId, Long requesterId, WorkReqDto dto) {
        Work work = workRepository.findByIdAndRequesterId(taskId, requesterId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);

        work.updateTitle(dto.getTitle());
        work.updateRecruitmentPeriod(dto.getRecruitmentPeriod());
        work.updateTaskDuration(dto.getTaskDuration());
        work.updatePrice(dto.getPrice());
        work.updateHeadCount(dto.getHeadCount());
        work.updateAcademicBackground(dto.getAcademicBackground());
        work.updatePreferred(dto.getPreferred());
        work.updateEtc(dto.getEtc());
        work.updateDescription(dto.getDescription());
        work.updateWorkEmail(dto.getWorkEmail());
        work.updateWorkPhoneNumber(dto.getWorkPhoneNumber());

        return work;
    }

    // 일거리 삭제
    @Transactional
    public void deleteWork(Long taskId, Long requesterId) {
        Work work = workRepository.findByIdAndRequesterId(taskId, requesterId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);
        workRepository.delete(work);
    }
}

