package com.ilkan.domain.work.scheduler;

import com.ilkan.domain.work.entity.Work;
import com.ilkan.domain.work.entity.enums.Status;
import com.ilkan.domain.work.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkStatusScheduler {

    private final WorkRepository workRepository;

    // 1분마다 실행
    @Scheduled(fixedRate = 60000)
    public void updateWorkStatus() {
        LocalDateTime now = LocalDateTime.now();

        // 시작해야 할 Work 찾기
        List<Work> toStart = workRepository.findByTaskStartBeforeAndStatus(now, Status.ASSIGNED);
        for (Work work : toStart) {
            work.updateStatus(Status.IN_PROGRESS);
        }

        // 종료해야 할 Work 찾기
        List<Work> toComplete = workRepository.findByTaskEndBeforeAndStatus(now, Status.IN_PROGRESS);
        for (Work work : toComplete) {
            work.updateStatus(Status.PAY_WAITING);
        }

        // 상태 변경 저장
        workRepository.saveAll(toStart);
        workRepository.saveAll(toComplete);
    }
}
