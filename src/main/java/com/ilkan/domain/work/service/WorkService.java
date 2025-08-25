package com.ilkan.domain.work.service;

import com.ilkan.domain.auth.repository.UserRepository;
import com.ilkan.domain.profile.dto.performer.WorkResDto;
import com.ilkan.domain.profile.entity.User;
import com.ilkan.domain.work.dto.WorkDetailResDto;
import com.ilkan.domain.work.dto.WorkListResDto;
import com.ilkan.domain.work.dto.performer.AlReadyAppliedResDto;
import com.ilkan.domain.work.dto.performer.WorkApplyReqDto;
import com.ilkan.domain.work.dto.requester.ApplicationResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyDetailResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyListResDto;
import com.ilkan.domain.work.dto.requester.WorkReqDto;
import com.ilkan.domain.work.entity.TaskApplication;
import com.ilkan.domain.work.entity.Work;
import com.ilkan.domain.work.entity.enums.Status;
import com.ilkan.domain.work.entity.enums.WorkCategory;
import com.ilkan.domain.work.repository.TaskApplicationRepository;
import com.ilkan.domain.work.repository.WorkRepository;
import com.ilkan.exception.UserWorkExceptions;
import com.ilkan.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * WorkService는 일거리(Work) 및 지원(TaskApplication) 관련 핵심 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 의뢰자(Requester)와 수행자(Performer)의 역할에 따라 접근 권한을 확인하며,
 * CRUD, 상태 전환, 지원 내역 조회 등의 기능을 제공합니다.
 */

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final TaskApplicationRepository taskApplicationRepository;


    /**
     * 의뢰자가 등록한 작업 목록 조회
     * @param role 사용자 역할 (REQUESTER만 허용)
     * @param pageable 페이지 정보
     * @return 등록한 Work를 WorkResDto로 변환한 Page 객체
     * @throws UserWorkExceptions.RequesterForbidden 권한이 없는 경우
     * @throws UserWorkExceptions.NoUploadedWorks 등록한 작업이 없는 경우
     */
    // 의뢰자가 등록한 작업 조회 (OPEN, ASSIGNED 상태만)
    @Transactional(readOnly = true)
    public Page<WorkResDto> getWorksByRequester(String role, Pageable pageable) {
        if (!"REQUESTER".equals(role)) {
            throw new UserWorkExceptions.RequesterForbidden();
        }

        Long requesterId = RoleMapper.getUserIdByRole(role);

        // 필터링 조건 (OPEN, ASSIGNED, APPLY_TO)
        List<Status> allowedStatuses = Arrays.asList(Status.OPEN, Status.ASSIGNED, Status.APPLY_TO);

        Page<Work> works = workRepository.findByRequesterIdAndStatusIn(requesterId, allowedStatuses, pageable);

        return works.map(WorkResDto::fromEntity);
    }


    /**
     // 의뢰자 진행중 목록 조회 (빈 Page 반환)
     */
    @Transactional(readOnly = true)
    public Page<WorkResDto> doingWorksByRequester(String roleHeader, Pageable pageable) {
        if (!"REQUESTER".equals(roleHeader)) {
            throw new UserWorkExceptions.RequesterForbidden();
        }
        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);

        Set<Status> activeStatuses = Status.RequesterActiveStatuses();
        Page<Work> works = workRepository.findByRequester_IdAndStatusIn(requesterId, activeStatuses, pageable);

        return works.map(WorkResDto::fromEntity);
    }

    /**
     * 수행자가 수행 중인 작업 목록 조회
     * @param roleHeader 사용자 역할 (PERFORMER만 허용)
     * @param pageable 페이지 정보
     * @return 수행 중인 Work를 WorkResDto로 변환한 Page 객체
     * @throws UserWorkExceptions.PerformerForbidden 권한이 없는 경우
     */
    // 수행자가 수행중인 작업 조회
    @Transactional(readOnly = true)
    public Page<WorkResDto> doingWorksByPerformer(String roleHeader, Pageable pageable) {
        if (!"PERFORMER".equals(roleHeader)) {
            throw new UserWorkExceptions.PerformerForbidden();
        }
        Long performerId = RoleMapper.getUserIdByRole(roleHeader);

        Set<Status> activeStatuses = Status.activeStatuses();
        Page<Work> works = workRepository.findByPerformer_IdAndStatusIn(performerId, activeStatuses, pageable);

        return works.map(WorkResDto::fromEntity); // 빈 Page면 그대로 내려감
    }

    /**
     * 수행자가 지원한 작업 목록 조회
     * @param role 사용자 역할 (PERFORMER만 허용)
     * @param pageable 페이지 정보
     * @return TaskApplication을 ApplicationResDto로 변환한 Page 객체
     * @throws UserWorkExceptions.PerformerForbidden 권한이 없는 경우
     */
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

        return applications.map(ApplicationResDto::fromEntity);
    }

    /**
     * 특정 카테고리 일거리 목록 조회
     * @param category WorkCategory
     * @param pageable 페이지 정보
     * @return WorkListResDto로 변환한 Page 객체
     */
    // 일거리 목록 조회 (카테고리 + OPEN 상태 필터)
    @Transactional(readOnly = true)
    public Page<WorkListResDto> getWorkList(WorkCategory category, Pageable pageable) {
        Page<Work> works;
        List<Status> visibleStatuses = Arrays.asList(Status.OPEN, Status.APPLY_TO);

        if (category == null) {
            // 전체 중에서 OPEN 또는 APPLY_TO인 것만 조회
            works = workRepository.findByStatusIn(visibleStatuses, pageable);
        } else {
            // 카테고리 + OPEN/APPLY_TO
            works = workRepository.findByWorkCategoryAndStatusIn(category, visibleStatuses, pageable);
        }


        return works.map(WorkListResDto::fromEntity);
    }


    /**
     * 일거리 상세 조회
     * @param taskId 조회할 작업 ID
     * @return WorkDetailResDto
     * @throws UserWorkExceptions.WorkNotFound 작업을 찾지 못한 경우
     */
    // 일거리 상세 조회 (예외처리 적용)
    @Transactional(readOnly = true)
    public WorkDetailResDto getWorkDetail(Long taskId) {
        Work work = workRepository.findById(taskId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);
        return WorkDetailResDto.fromEntity(work);
    }

    /**
     * 새로운 일거리 등록
     * @param roleHeader 사용자 역할 헤더
     * @param dto WorkReqDto
     * @return 저장된 Work
     * @throws UserWorkExceptions.RequesterForbidden 권한이 없는 경우
     */
    // 일거리 등록
    @Transactional
    public Work createWork(String roleHeader, WorkReqDto dto) {
        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        User requester = userRepository.findById(requesterId)
                .orElseThrow(UserWorkExceptions.RequesterForbidden::new);
        Work work = dto.toEntity(requester);
        return workRepository.save(work);
    }

    /**
     * 기존 일거리 수정
     * @param taskId 수정할 작업 ID
     * @param roleHeader 사용자 역할 헤더
     * @param dto WorkReqDto
     * @return 수정된 Work
     * @throws UserWorkExceptions.WorkNotFound 작업을 찾지 못한 경우
     */
    // 일거리 수정
    @Transactional
    public Work updateWork(Long taskId, String roleHeader, WorkReqDto dto) {
        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        Work work = workRepository.findByIdAndRequesterId(taskId, requesterId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);
        work.updateFromDto(dto);
        return work;
    }

    /**
     * 일거리 삭제
     * @param taskId 삭제할 작업 ID
     * @param roleHeader 사용자 역할 헤더
     * @throws UserWorkExceptions.WorkNotFound 작업을 찾지 못한 경우
     */
    // 일거리 삭제
    @Transactional
    public void deleteWork(Long taskId, String roleHeader) {
        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        Work work = workRepository.findByIdAndRequesterId(taskId, requesterId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);
        workRepository.delete(work);
    }

    /**
     * 수행자가 일거리 신청
     * @param roleHeader 사용자 역할 헤더
     * @param taskId 신청할 작업 ID
     * @param dto WorkApplyReqDto
     * @return 저장된 ApplicationResDto
     * @throws UserWorkExceptions.PerformerForbidden 권한이 없는 경우
     * @throws UserWorkExceptions.WorkNotFound 작업을 찾지 못한 경우
     */
    // 수행자 일거리 신청 (서비스)
    @Transactional
    public AlReadyAppliedResDto applyWork(String roleHeader, Long taskId, WorkApplyReqDto dto) {
        if (!"PERFORMER".equals(roleHeader)) {
            throw new UserWorkExceptions.PerformerForbidden();
        }

        Long performerId = RoleMapper.getUserIdByRole(roleHeader);

        // 1) Work 조회
        Work work = workRepository.findById(taskId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);

        // 2) 중복 지원 체크 (ID 기준)
        boolean alreadyApplied = taskApplicationRepository.existsByTaskId_IdAndPerformerId_Id(taskId, performerId);
        if (alreadyApplied) {
            // 이미 지원한 경우: 기존 지원서 가져와서 DTO로 포장
            TaskApplication existing = taskApplicationRepository
                    .findFirstByTaskId_IdAndPerformerId_IdOrderByAppliedAtDesc(taskId, performerId)
                    .orElseThrow(() -> new IllegalStateException("지원 기록이 존재하지만 조회에 실패했습니다."));
            ApplicationResDto appDto = ApplicationResDto.fromEntity(existing);
            return AlReadyAppliedResDto.builder()
                    .alreadyApplied(true)
                    .application(appDto)
                    .message("이미 지원한 일거리입니다!")
                    .build();
        }

        // 3) Performer(User) 조회
        User performer = userRepository.findById(performerId)
                .orElseThrow(UserWorkExceptions.PerformerForbidden::new);

        // 4) Work 상태 변경
        work.updateStatus(Status.APPLY_TO);
        workRepository.save(work);

        // 5) TaskApplication 생성 및 저장
        TaskApplication application = dto.toEntity(work, performer);
        application.updateStatus(work.getStatus());
        taskApplicationRepository.save(application);

        // 6) DTO 변환 후 반환 (alreadyApplied = false)
        ApplicationResDto appDto = ApplicationResDto.fromEntity(application);
        return AlReadyAppliedResDto.builder()
                .alreadyApplied(false)
                .application(appDto)
                .message("지원 완료되었습니다.")
                .build();
    }




    /**
     * 의뢰자 기준 수행자 지원 목록 조회
     * @param roleHeader 사용자 역할 헤더
     * @param pageable 페이지 정보
     * @return WorkApplyListResDto Page 객체
     * @throws UserWorkExceptions.RequesterForbidden 권한이 없는 경우
     */
    // 의뢰자 기준 수행자들이 지원한 지원서 목록 조회
    @Transactional(readOnly = true)
    public Page<WorkApplyListResDto> getApplicantsByRequester(String roleHeader, Long taskId, Pageable pageable) {
        if (!"REQUESTER".equals(roleHeader)) {
            throw new UserWorkExceptions.RequesterForbidden();
        }

        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        Page<TaskApplication> applications = taskApplicationRepository
                .findByTaskId_Id(taskId, pageable);

        return applications.map(WorkApplyListResDto::fromEntity);
    }

    /**
     * 의뢰자 기준 수행자 지원 상세 조회
     * @param roleHeader 사용자 역할 헤더
     * @param workId 작업 ID
     * @param performerId 지원 ID
     * @return WorkApplyDetailResDto
     * @throws UserWorkExceptions.RequesterForbidden 권한이 없는 경우
     * @throws UserWorkExceptions.WorkNotFound 작업을 찾지 못한 경우
     * @throws UserWorkExceptions.InvalidRequest 다른 의뢰자의 일거리 접근 시
     * @throws UserWorkExceptions.NoAppliedWorks 지원 내역이 없는 경우
     */
    // 의뢰자기준 수행자들이 지원한 지원서 상세 조회
    @Transactional(readOnly = true)
    public WorkApplyDetailResDto getWorkApplyDetail(String roleHeader, Long workId, Long performerId) {
        if (!"REQUESTER".equals(roleHeader)) {
            throw new UserWorkExceptions.RequesterForbidden();
        }

        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);
        Work work = workRepository.findById(workId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new); // 삭제된 일거리 접근 시 안전하게 처리하기 위한 방어 코드

        if (!work.getRequester().getId().equals(requesterId)) {
            throw new UserWorkExceptions.InvalidRequest("다른 의뢰자의 일거리 입니다."); // 다른 의뢰자의 작업 접근 불가
        }

        TaskApplication application = taskApplicationRepository
                .findByPerformerId_IdAndTaskId_Id(performerId, workId)
                .orElseThrow(UserWorkExceptions.NoAppliedWorks::new);

        return WorkApplyDetailResDto.fromEntity(application);
    }

    /**
     * 의뢰자가 수행자 선택 및 승인
     * @param roleHeader 사용자 역할 헤더
     * @param taskId 작업 ID
     * @param performerId 선택할 수행자 ID
     * @return WorkApplyListResDto
     * @throws UserWorkExceptions.RequesterForbidden 권한이 없는 경우
     * @throws UserWorkExceptions.InvalidRequest 다른 의뢰자의 일거리 접근 시
     */
    // 의뢰자가 수행자 선택
    @Transactional
    public WorkApplyListResDto approvePerformer(String roleHeader, Long taskId, Long performerId) {
        if (!"REQUESTER".equals(roleHeader)) {
            throw new UserWorkExceptions.RequesterForbidden();
        }

        Long requesterId = RoleMapper.getUserIdByRole(roleHeader);

        Work work = workRepository.findById(taskId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);

        if (!work.getRequester().getId().equals(requesterId)) {
            throw new UserWorkExceptions.InvalidRequest("다른 의뢰자의 일거리입니다.");
        }

        User performer = userRepository.findById(performerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 수행자를 찾을 수 없습니다."));

        TaskApplication application = taskApplicationRepository
                .findByTaskIdAndPerformerId(work, performer)
                .orElseThrow(() -> new IllegalArgumentException("해당 지원 내역이 없습니다."));

        work.updatePerformer(performer);
        // 상태는 ASSIGNED로 변경 (배정됨). 준비완료 플래그는 false로 초기화.
        work.updateStatus(Status.ASSIGNED);
        work.updatePerformerReady(false);
        work.updateRequesterReady(false);
        work.updatePerformerDone(false);
        work.updateRequesterPaid(false);
        workRepository.save(work);

        return WorkApplyListResDto.fromEntity(application);
    }


}