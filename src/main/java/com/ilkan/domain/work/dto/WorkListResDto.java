package com.ilkan.domain.work.dto;

import com.ilkan.domain.work.entity.Work;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "일거리목록 DTO")
public class WorkListResDto {

    @Schema(description = "일거리 id", example = "1")
    private final Long taskId;

    @Schema(description = "기업명/공고", example = "[카페 반절] 인스타분위기 카페 로고 디자인 외주 의뢰")
    private final String title; // 기업명/공고

    @Schema(description = "보수", example = "5000원~")
    private final Long price; // 보수

    @Schema(description = "모집 기한", example = "~25/08/30")
    private final LocalDateTime recruitmentPeriod; // 기한

    public static WorkListResDto fromEntity(Work work){
        return WorkListResDto.builder()
                .taskId(work.getId())
                .title(work.getTitle())
                .price(work.getPrice())
                .recruitmentPeriod(work.getRecruitmentPeriod())
                .build();
    }

}
