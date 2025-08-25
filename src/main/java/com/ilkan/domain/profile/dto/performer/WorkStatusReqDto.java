package com.ilkan.domain.profile.dto.performer;

import com.ilkan.domain.work.entity.Work;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "수행자 프로필 일거리 상태 변경DTO")
public class WorkStatusReqDto {

    @Schema(description = "작업 시작 시간", example = "2025-08-20T09:00:00")
    private LocalDateTime taskStart;

    @Schema(description = "작업 마감 시간", example = "2025-08-22T18:00:00")
    private LocalDateTime taskEnd;

    public void applyTo(Work work) {
        if (taskStart != null) work.updateTaskStart(taskStart);
        if (taskEnd != null) work.updateTaskEnd(taskEnd);

        // 간단한 검증: 시작 <= 종료
        LocalDateTime s = work.getTaskStart();
        LocalDateTime e = work.getTaskEnd();
        if (s != null && e != null && s.isAfter(e)) {
            throw new IllegalArgumentException("taskStart must be before or equal to taskEnd");
        }
    }
}
