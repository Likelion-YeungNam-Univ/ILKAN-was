package com.ilkan.dto.workdto;

import com.ilkan.domain.entity.TaskApplication;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "의뢰자 기준 수행자 일거리 지원목록 조회 DTO")
public class WorkApplyListResDto {

    @Schema(description = "수행자명", example = "김야호")
    private final String performerName;

    @Schema(description = "일거리 제목", example = "디자인 로고 생성")
    private final String workTitle;

    @Schema(description = "포트폴리오 URL", example = "https://~~~~")
    private final String portfolioUrl;

    public static WorkApplyListResDto fromEntity(TaskApplication application) {
        return WorkApplyListResDto.builder()
                .performerName(application.getPerformerId().getName()) // 수행자 이름
                .workTitle(application.getTaskId().getTitle())         // 일거리 제목
                .portfolioUrl(application.getPortfolioUrl())           // 포트폴리오 URL
                .build();
    }
}
