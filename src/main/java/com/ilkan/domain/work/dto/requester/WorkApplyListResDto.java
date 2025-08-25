package com.ilkan.domain.work.dto.requester;

import com.ilkan.domain.work.entity.TaskApplication;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "의뢰자 기준 , 수행자 일거리 지원목록 조회 / 수행자 선택 DTO")
public class WorkApplyListResDto {

    @Schema(description = "수행자 ID", example = "42")
    private final Long performerId;

    @Schema(description = "수행자명", example = "김야호")
    private final String performerName;

    @Schema(description = "자기소개", example = "안녕하세요 저는~")
    private final String bio;

    @Schema(description = "포트폴리오 URL", example = "https://~~~~")
    private final String portfolioUrl;

    public static WorkApplyListResDto fromEntity(TaskApplication application) {
        return WorkApplyListResDto.builder()
                .performerId(application.getPerformerId().getId())
                .performerName(application.getPerformerId().getName()) // 수행자 이름
                .bio(application.getBio())
                .portfolioUrl(application.getPortfolioUrl())           // 포트폴리오 URL
                .build();
    }
}
