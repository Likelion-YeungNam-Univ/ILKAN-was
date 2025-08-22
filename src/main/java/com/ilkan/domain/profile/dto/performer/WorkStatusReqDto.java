package com.ilkan.domain.profile.dto.performer;

import com.ilkan.domain.work.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "수행자 프로필 일거리 상태 변경DTO")
public class WorkStatusReqDto {

    @Schema(description = "일거리 상태", example = "IN_PROGRESS")
    private Status status;

    @Schema(description = "작업 시작 시간", example = "2025-08-20T09:00:00")
    private LocalDateTime taskStart;

    @Schema(description = "작업 마감 시간", example = "2025-08-22T18:00:00")
    private LocalDateTime taskEnd;

}
