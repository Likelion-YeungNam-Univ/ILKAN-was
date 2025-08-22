package com.ilkan.domain.work.service;

import com.ilkan.domain.profile.dto.performer.WorkResDto;
import com.ilkan.domain.profile.dto.performer.WorkStatusReqDto;
import com.ilkan.domain.work.entity.Work;
import com.ilkan.domain.work.entity.enums.Status;
import com.ilkan.domain.work.repository.WorkRepository;
import com.ilkan.exception.UserWorkExceptions;
import com.ilkan.util.RoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class WorkSetStatusService {
    private final WorkRepository workRepository;

    /**
     * 상태 변경 공용 메서드
     * roleHeader: "REQUESTER" 또는 "PERFORMER"
     */
    @Transactional
    public WorkResDto updateWorkStatus(String roleHeader, Long workId, WorkStatusReqDto workStatusReqDto) {
        Work work = workRepository.findById(workId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);

        // 작업 시간 업데이트
        if (workStatusReqDto.getTaskStart() != null) work.updateTaskStart(workStatusReqDto.getTaskStart());
        if (workStatusReqDto.getTaskEnd() != null) work.updateTaskEnd(workStatusReqDto.getTaskEnd());


        // 현재 상태
        Status current = work.getStatus();

        // 사용자 id (role -> userId 매핑)
        Long userId = RoleMapper.getUserIdByRole(roleHeader);

        // ---- ASSIGNED: 준비완료 플래그 처리
        if (current == Status.ASSIGNED) {
            if ("REQUESTER".equals(roleHeader)) {
                if (!work.getRequester().getId().equals(userId)) {
                    throw new UserWorkExceptions.RequesterForbidden();
                }
                work.updateRequesterReady(true);
            } else if ("PERFORMER".equals(roleHeader)) {
                if (work.getPerformer() == null || !work.getPerformer().getId().equals(userId)) {
                    throw new UserWorkExceptions.PerformerForbidden();
                }
                work.updatePerformerReady(true);
            } else {
                throw new UserWorkExceptions.InvalidRequest("권한 없음");
            }

            // 둘 다 준비되었을 때 IN_PROGRESS로 전환
            if (work.isRequesterReady() && work.isPerformerReady()) {
                work.updateStatus(Status.IN_PROGRESS);
            }

            workRepository.save(work);
            return WorkResDto.fromEntity(work);
        }

        // ---- IN_PROGRESS: 수행자 수행완료 -> PAY_WAITING
        else if (current == Status.IN_PROGRESS) {
            if ("PERFORMER".equals(roleHeader)) {
                if (!work.isPerformerDone()) {
                    work.updatePerformerDone(true);
                }
                work.updateStatus(Status.PAY_WAITING);
            } else if ("REQUESTER".equals(roleHeader)) {
                if (!work.isRequesterPaid()) {
                    work.updateRequesterPaid(true);
                }
                work.updateStatus(Status.COMPLETE_WAITING);
            } else {
                throw new UserWorkExceptions.InvalidRequest("권한 없음");
            }

            workRepository.save(work);
            return WorkResDto.fromEntity(work);
        }

        // ---- PAY_WAITING: 의뢰자가 완료 클릭 시
        else if (current == Status.PAY_WAITING) {
            if ("REQUESTER".equals(roleHeader)) {
                if (!work.isRequesterPaid()) {
                    work.updateRequesterPaid(true);
                }
                work.updateStatus(Status.COMPLETED);
                workRepository.save(work);
                return WorkResDto.fromEntity(work);
            } else {
                throw new UserWorkExceptions.InvalidRequest("PAY_WAITING 상태에서는 의뢰자만 완료 가능");
            }
        }

        // ---- COMPLETE_WAITING: 수행자가 완료 클릭 시
        else if (current == Status.COMPLETE_WAITING) {
            if ("PERFORMER".equals(roleHeader)) {
                if (!work.isPerformerDone()) {
                    work.updatePerformerDone(true);
                }
                work.updateStatus(Status.COMPLETED);
                workRepository.save(work);
                return WorkResDto.fromEntity(work);
            } else {
                throw new UserWorkExceptions.InvalidRequest("COMPLETE_WAITING 상태에서는 수행자만 완료 가능");
            }
        }

        // 그 외 상태는 변경 불가
        else {
            throw new UserWorkExceptions.InvalidRequest("현재 상태에서 요청한 상태로 변경할 수 없습니다.");
        }
    }
}
