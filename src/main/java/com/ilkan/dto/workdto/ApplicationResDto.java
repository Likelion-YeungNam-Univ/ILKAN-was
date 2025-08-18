package com.ilkan.dto.workdto;

import com.ilkan.domain.entity.TaskApplication;
import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@Schema(description = "일거리 지원조회 DTO")
public class ApplicationResDto {

    @Schema(description = "일거리 id", example = "1")
    private final Long taskId;

    @Schema(description = "일거리 상태", example = "APPLY-TO")
    private final Status status;

    @Schema(description = "기업명/공고", example = "[카페 반절] 인스타분위기 카페 로고 디자인 외주 의뢰")
    private final String title; // 기업명/공고

    @Schema(description = "보수", example = "5000원~")
    private final Long price; // 보수

    @Schema(description = "모집 기한", example = "~25/08/30")
    private final LocalDateTime recruitmentPeriod; // 기한

    public static ApplicationResDto fromEntity(TaskApplication app) {
        Work work = app.getTaskId(); // Work 객체 가져오기
        return ApplicationResDto.builder()
                .taskId(work.getId())
                .status(app.getStatus())
                .title(work.getTitle())
                .price(work.getPrice())
                .recruitmentPeriod(work.getRecruitmentPeriod())
                .build();
    }

}
