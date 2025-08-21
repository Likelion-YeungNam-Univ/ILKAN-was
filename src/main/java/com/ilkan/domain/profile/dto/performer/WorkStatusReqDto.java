package com.ilkan.domain.profile.dto.performer;

import com.ilkan.domain.work.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "수행자 프로필 일거리 상태 변경DTO")
public class WorkStatusReqDto {
    @Schema(description = "일거리 상태", example = "IN_PROGRESS")
    private Status status;
}
