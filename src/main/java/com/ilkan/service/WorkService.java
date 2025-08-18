package com.ilkan.service;

import com.ilkan.domain.entity.TaskApplication;
import com.ilkan.domain.entity.User;
import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import com.ilkan.dto.workdto.ApplicationResDto;
import com.ilkan.dto.workdto.WorkApplyDetailResDto;
import com.ilkan.dto.workdto.WorkApplyListResDto;
import com.ilkan.dto.workdto.WorkApplyReqDto;
import com.ilkan.dto.workdto.WorkDetailResDto;
import com.ilkan.dto.workdto.WorkListResDto;
import com.ilkan.dto.workdto.WorkReqDto;
import com.ilkan.dto.workdto.WorkResDto;
import com.ilkan.exception.UserWorkExceptions;
import com.ilkan.repository.TaskApplicationRepository;
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
    private final TaskApplicationRepository taskApplicationRepository;

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
    public Page<ApplicationResDto> getAppliedWorksByPerformer(String role, Pageable pageable) {
        if (!"PERFORMER".equals(role)) {
            throw new UserWorkExceptions.PerformerForbidden();
        }

        Long performerId = RoleMapper.getUserIdByRole(role);

        // TaskApplication 기준으로 조회
        Page<TaskApplication> applications =
                taskApplicationRepository.findByPerformerId_IdAndStatus(performerId, Status.APPLY_TO, pageable);

        if (applications.isEmpty()) {
            throw new UserWorkExceptions.NoAppliedWorks(); // 지원한 일거리 없음 예외
        }

        return applications.map(ApplicationResDto::fromEntity);
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
    public Work createWork(String roleHeader, WorkReqDto dto) {
        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        User requester = userRepository.findById(requesterId)
                .orElseThrow(UserWorkExceptions.RequesterForbidden::new);
        Work work = dto.toEntity(requester);
        return workRepository.save(work);
    }

    // 일거리 수정
    @Transactional
    public Work updateWork(Long taskId, String roleHeader, WorkReqDto dto) {
        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        Work work = workRepository.findByIdAndRequesterId(taskId, requesterId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);
        work.updateFromDto(dto);
        return work;
    }

    // 일거리 삭제
    @Transactional
    public void deleteWork(Long taskId, String roleHeader) {
        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        Work work = workRepository.findByIdAndRequesterId(taskId, requesterId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);
        workRepository.delete(work);
    }

    // 수행자 일거리 신청
    @Transactional
    public TaskApplication applyWork(String roleHeader, Long taskId, WorkApplyReqDto dto) {
        if (!"PERFORMER".equals(roleHeader)) {
            throw new UserWorkExceptions.PerformerForbidden();
        }

        Long performerId = RoleMapper.getUserIdByRole(roleHeader);
        Work work = workRepository.findById(taskId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);
        User performer = userRepository.findById(performerId)
                .orElseThrow(UserWorkExceptions.PerformerForbidden::new);

        return taskApplicationRepository.save(dto.toEntity(work, performer));
    }

    // 의뢰자 기준 수행자들이 지원한 지원서 목록 조회
    @Transactional(readOnly = true)
    public Page<WorkApplyListResDto> getApplicantsByRequester(String roleHeader, Pageable pageable) {
        if (!"REQUESTER".equals(roleHeader)) {
            throw new UserWorkExceptions.RequesterForbidden();
        }

        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        Page<TaskApplication> applications = taskApplicationRepository
                .findByTaskId_Requester_IdAndStatus(requesterId, Status.APPLY_TO, pageable);

        return applications.map(WorkApplyListResDto::fromEntity);
    }


    // 의뢰자기준 수행자들이 지원한 지원서 상세 조회
    @Transactional(readOnly = true)
    public WorkApplyDetailResDto getWorkApplyDetail(String roleHeader, Long workId, Long applyId) {
        if (!"REQUESTER".equals(roleHeader)) {
            throw new UserWorkExceptions.RequesterForbidden();
        }

        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        Work work = workRepository.findById(workId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new); // 삭제된 일거리 접근 시 안전하게 처리하기 위한 방어 코드

        if (!work.getRequester().getId().equals(requesterId)) {
            throw new UserWorkExceptions.InvalidRequest("다른 의뢰자의 일거리 입니다."); // 다른 의뢰자의 작업 접근 불가
        }

        TaskApplication application = taskApplicationRepository.findById(applyId)
                .orElseThrow(UserWorkExceptions.NoAppliedWorks::new);
        return WorkApplyDetailResDto.fromEntity(application);


    }
}



