package com.ilkan.domain.work.dto.performer;

import com.ilkan.domain.work.dto.requester.ApplicationResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "일거리 지원 및 중복방지에 사용하는 Dto")
public class AlReadyAppliedResDto {

    @Schema(description = "이미 신청한 여부", example = "true")
    private final boolean alreadyApplied;

    @Schema(description = "신규 지원 시 response", example = "지원 정보 쭉")
    private final ApplicationResDto application;

    @Schema(description = "중복지원 / 지원완료 메시지", example = "이미 지원된 일거리 입니다!")
    private final String message;
}
