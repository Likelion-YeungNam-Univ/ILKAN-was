package com.ilkan.domain.work.dto.requester;

import com.ilkan.domain.profile.entity.User;
import com.ilkan.domain.work.entity.Work;
import com.ilkan.domain.work.entity.enums.Status;
import com.ilkan.domain.work.entity.enums.WorkCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "의뢰자 일거리 등록/수정 DTO")
public class WorkReqDto {

    @Schema(description = "제목", example = "[카페 반절] 인스타 BI 로고 디자인")
    private final String title;

    @Schema(description = "공고 기한", example = " ~ 2025-09-15")
    private final LocalDateTime recruitmentPeriod;

    @Schema(description = "작업 기간", example = " 3")
    private final String taskDuration;

    @Schema(description = "보수", example = "5000")
    private final Long price;

    @Schema(description = "이메일", example = "adsf@naver.com")
    private final String workEmail;

    @Schema(description = "전화", example = " 010-1234-1243")
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

    @Schema(description = "일거리 카테고리", example = "DESIGN")
    private final WorkCategory category;


    public Work toEntity(User requester) {
        return Work.builder()
                .title(this.title)
                .recruitmentPeriod(this.recruitmentPeriod)
                .taskDuration(this.taskDuration)
                .price(this.price)
                .headCount(this.headCount)
                .academicBackGround(this.academicBackground)
                .preferred(this.preferred)
                .etc(this.etc)
                .description(this.description)
                .workEmail(this.workEmail)
                .workPhoneNumber(this.workPhoneNumber)
                .createdAt(LocalDateTime.now())
                .requester(requester)
                .workCategory(this.category) // 추가
                .status(Status.OPEN) // 기본상태는 모집중으로
                .build();
    }
}
