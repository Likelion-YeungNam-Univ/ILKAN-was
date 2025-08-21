package com.ilkan.domain.work.service;

import com.ilkan.domain.profile.dto.performer.WorkResDto;
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
     // 상태 변경 공용 메서드
     // roleHeader: "REQUESTER" 또는 "PERFORMER"
     */
    @Transactional
    public WorkResDto updateWorkStatus(String roleHeader, Long workId, Status requestedStatus) {
        // 작업 조회
        Work work = workRepository.findById(workId)
                .orElseThrow(UserWorkExceptions.WorkNotFound::new);

        // 현재 상태
        Status current = work.getStatus();

        // 사용자 id (role -> userId 매핑)
        Long userId = RoleMapper.getUserIdByRole(roleHeader);

        // ---- ASSIGNED: 준비완료 플래그 처리 (프론트는 requestedStatus = IN_PROGRESS 로 보냄)
        if (current == Status.ASSIGNED && requestedStatus == Status.IN_PROGRESS) {
            if ("REQUESTER".equals(roleHeader)) {
                // 의뢰자만 requesterReady 누를 수 있음(소유자 체크)
                if (!work.getRequester().getId().equals(userId)) {
                    throw new UserWorkExceptions.RequesterForbidden();
                }
                if (!work.isRequesterReady()) {
                    work.updateRequesterReady(true);
                }
            } else if ("PERFORMER".equals(roleHeader)) {
                // 수행자 플래그 처리
                 if (work.getPerformer() == null || !work.getPerformer().getId().equals(userId)) {
                     throw new UserWorkExceptions.PerformerForbidden();
                 }
                if (!work.isPerformerReady()) {
                    work.updatePerformerReady(true);
                }
            } else {
                throw new UserWorkExceptions.InvalidRequest("권한 없음");
            }

            // 둘 다 준비되었을 때 IN_PROGRESS로 전환
            if (work.isRequesterReady() && work.isPerformerReady()) {
                work.updateStatus(Status.IN_PROGRESS);
            }

            workRepository.save(work); // 명시적 저장
            return WorkResDto.fromEntity(work);
        }

        // ---- IN_PROGRESS: 수행자 수행완료 -> PAY_WAITING, 의뢰자 보수지급 -> COMPLETE_WAITING ----
        else if (current == Status.IN_PROGRESS) {
            // 수행자 수행완료
            if ("PERFORMER".equals(roleHeader) && requestedStatus == Status.PAY_WAITING) {
                if (!work.isPerformerDone()) {
                    work.updatePerformerDone(true);
                }
                work.updateStatus(Status.PAY_WAITING);
                workRepository.save(work);
                return WorkResDto.fromEntity(work);
            }

            // 의뢰자 보수지급 (의뢰자가 먼저 지급 누르면 COMPLETE_WAITING)
            if ("REQUESTER".equals(roleHeader) && requestedStatus == Status.COMPLETE_WAITING) {
                if (!work.getRequester().getId().equals(userId)) {
                    throw new UserWorkExceptions.RequesterForbidden();
                }
                if (!work.isRequesterPaid()) {
                    work.updateRequesterPaid(true);
                }
                work.updateStatus(Status.COMPLETE_WAITING);
                workRepository.save(work);
                return WorkResDto.fromEntity(work);
            }

            throw new UserWorkExceptions.InvalidRequest("IN_PROGRESS 상태에서 잘못된 요청입니다.");
        }

        // ---- PAY_WAITING: 수행자가 먼저 눌러서 PAY_WAITING 상태일 때 의뢰자가 COMPLETED 누르면 완료 ----
        else if (current == Status.PAY_WAITING) {
            if ("REQUESTER".equals(roleHeader) && requestedStatus == Status.COMPLETED) {
                if (!work.getRequester().getId().equals(userId)) {
                    throw new UserWorkExceptions.RequesterForbidden();
                }
                if (!work.isRequesterPaid()) {
                    work.updateRequesterPaid(true);
                }
                work.updateStatus(Status.COMPLETED);
                workRepository.save(work);
                return WorkResDto.fromEntity(work);
            } else {
                throw new UserWorkExceptions.InvalidRequest("PAY_WAITING 상태에서는 의뢰자만 COMPLETED로 변경할 수 있습니다.");
            }
        }

        // ---- COMPLETE_WAITING: 의뢰자가 먼저 지급 눌러서 COMPLETE_WAITING 상태일 때 수행자가 COMPLETED 누르면 완료 ----
        else if (current == Status.COMPLETE_WAITING) {
            if ("PERFORMER".equals(roleHeader) && requestedStatus == Status.COMPLETED) {
                if (!work.isPerformerDone()) {
                    work.updatePerformerDone(true);
                }
                work.updateStatus(Status.COMPLETED);
                workRepository.save(work);
                return WorkResDto.fromEntity(work);
            } else {
                throw new UserWorkExceptions.InvalidRequest("COMPLETE_WAITING 상태에서는 수행자만 COMPLETED로 변경할 수 있습니다.");
            }
        }

        // 그 외 상태는 변경 불가
        else {
            throw new UserWorkExceptions.InvalidRequest("현재 상태에서 요청한 상태로 변경할 수 없습니다.");
        }
    }
}
