package com.ilkan.dto.workdto;

import com.ilkan.domain.entity.TaskApplication;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "지원서 상세조회 응답 DTO")
public class WorkApplyDetailResDto {

    @Schema(description = "지원 기록 ID", example = "10")
    private final Long applyId;

    @Schema(description = "일거리 ID", example = "3")
    private final Long taskId;

    @Schema(description = "일거리 제목", example = "디자인 로고 생성")
    private final String workTitle;

    @Schema(description = "수행자 ID", example = "7")
    private final Long performerId;

    @Schema(description = "수행자명", example = "김야호")
    private final String performerName;

    @Schema(description = "자기소개", example = "안녕하세요, 저는 디자인 경험이 많습니다.")
    private final String bio;

    @Schema(description = "포트폴리오 URL", example = "https://portfolio.me/kim")
    private final String portfolioUrl;

    public static WorkApplyDetailResDto fromEntity(TaskApplication application) {
        return WorkApplyDetailResDto.builder()
                .applyId(application.getId())
                .taskId(application.getTaskId().getId())
                .workTitle(application.getTaskId().getTitle())
                .performerId(application.getPerformerId().getId())
                .performerName(application.getPerformerId().getName())
                .bio(application.getBio())
                .portfolioUrl(application.getPortfolioUrl())
                .build();
    }
}
