package com.ilkan.dto.workdto;

import com.ilkan.domain.entity.Work;
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

    @Schema(description = "기업명/공고", example = "[카페 반절] 인스타분위기 카페 로고 디자인 외주 의뢰")
    private final String title; // 기업명/공고

    @Schema(description = "보수", example = "5000원~")
    private final Long price; // 보수

    @Schema(description = "기한", example = "~25/08/30")
    private final LocalDateTime taskEnd; // 기한

    public static WorkListResDto fromEntity(Work work){
        return WorkListResDto.builder()
                .title(work.getTitle())
                .price(work.getPrice())
                .taskEnd(work.getTaskEnd())
                .build();
    }

}
