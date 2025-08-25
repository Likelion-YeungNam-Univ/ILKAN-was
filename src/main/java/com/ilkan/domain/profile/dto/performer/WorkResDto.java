package com.ilkan.domain.profile.dto.performer;

import com.ilkan.domain.work.entity.Work;
import com.ilkan.domain.work.entity.enums.Status;
import com.ilkan.domain.auth.dto.UserResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "일거리조회 DTO")
public class WorkResDto {

    @Schema(description = "일거리 ID", example = "123")
    private final Long taskId;

    @Schema(description = "의뢰자 정보")
    private final UserResDto requester;

    @Schema(description = "선택된 수행자 정보", example = "김이박")
    private final UserResDto performer;

    @Schema(description = "제목", example = "홈페이지 제작")
    private final String title;

    @Schema(description = "상세 설명", example = "기업 홈페이지 리뉴얼 프로젝트")
    private final String description;

    @Schema(description = "등록 시간", example = "2025-08-16T15:30:00")
    private final LocalDateTime createdAt;

    @Schema(description = "가격", example = "500000")
    private final Long price;

    @Schema(description = "일거리 상태", example = "OPEN")
    private final Status status;

    @Schema(description = "작업 시작 시간", example = "2025-08-20T09:00:00")
    private final LocalDateTime taskStart;

    @Schema(description = "작업 마감 시간", example = "2025-08-22T18:00:00")
    private final LocalDateTime taskEnd;

    @Schema(description = "모집 기한", example = "~25/08/30")
    private final LocalDateTime recruitmentPeriod; // 모집 기한

    @Schema(description = "수행자 준비완료 버튼 클릭 여부")
    private final boolean performerReady;


    // DB에서 조회한 Entity를 API 응답용 DTO로 변환하기 위함
    public static WorkResDto fromEntity(Work work) {
        return WorkResDto.builder()
                .taskId(work.getId())
                .requester(UserResDto.fromEntity(work.getRequester()))
                .performer(work.getPerformer() != null ? UserResDto.fromEntity(work.getPerformer()) : null)
                .title(work.getTitle())
                .description(work.getDescription())
                .createdAt(work.getCreatedAt())
                .price(work.getPrice())
                .status(work.getStatus())
                .taskStart(work.getTaskStart())
                .taskEnd(work.getTaskEnd())
                .recruitmentPeriod(work.getRecruitmentPeriod())
                .performerReady(work.isPerformerReady())
                .build();
    }
}
