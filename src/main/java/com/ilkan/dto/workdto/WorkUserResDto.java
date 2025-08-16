package com.ilkan.dto.workdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "의뢰자 일거리 등록/조회용 DTO")
public class WorkUserResDto {

    @Schema(description = "일거리 ID", example = "14")
    private final Long taskId;

    @Schema(description = "제목", example = "[카페 반절] 인스타 BI 로고 디자인")
    private final String title;

    @Schema(description = "공고 기한", example = "2025-09-15T00:00:00")
    private final LocalDateTime recruitmentPeriod;

    @Schema(description = "작업 기간", example = "3개월")
    private final String taskDuration;

    @Schema(description = "보수", example = "5000")
    private final Long price;

    @Schema(description = "이메일", example = "adsf@naver.com")
    private final String workEmail;

    @Schema(description = "전화", example = "010-1234-1243")
    private final String workPhoneNumber;

    @Schema(description = "모집 인원", example = "1")
    private final Long headCount;

    @Schema(description = "학력", example = "대졸 이상")
    private final String academicBackground;

    @Schema(description = "우대사항", example = "AWS 배포 경험")
    private final String preferred;

    @Schema(description = "기타조건", example = "포트폴리오 필수")
    private final String etc;

    @Schema(description = "상세설명", example = "의뢰내용 : ----- / 지원자격 : ----")
    private final String description;

    @Schema(description = "생성일", example = "2025-08-15T20:36:37")
    private final LocalDateTime createdAt;

    public static WorkUserResDto fromEntity(com.ilkan.domain.entity.Work work) {
        return WorkUserResDto.builder()
                .taskId(work.getId())
                .title(work.getTitle())
                .recruitmentPeriod(work.getRecruitmentPeriod())
                .taskDuration(work.getTaskDuration())
                .price(work.getPrice())
                .workEmail(work.getWorkEmail())
                .workPhoneNumber(work.getWorkPhoneNumber())
                .headCount(work.getHeadCount())
                .academicBackground(work.getAcademicBackGround())
                .preferred(work.getPreferred())
                .etc(work.getEtc())
                .description(work.getDescription())
                .createdAt(work.getCreatedAt())
                .build();
    }
}
