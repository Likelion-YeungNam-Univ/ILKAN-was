package com.ilkan.dto.workdto;

import com.ilkan.domain.entity.User;
import com.ilkan.domain.entity.Work;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "일거리 상세조회 DTO")
public class WorkDetailResDto {

    @Schema(description = "일거리 id", example = "1")
    private Long taskId;

    @Schema(description = "제목", example = "[카페 반절] 인스타 분위기 카페 BI 로고 디자인 외주 의뢰")
    private String title;

    @Schema(description = "작업기간", example = "~3개월")
    private String taskDuration;

    @Schema(description = "작업보수", example = "500원~")
    private Long price;

    @Schema(description = "이메일", example = "adf@naver.com")
    private String eMail;

    @Schema(description = "연락처", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "모집인원", example = "1명")
    private final Long headcount;

    @Schema(description = "학력", example = "박사")
    private final String academicBackground;

    @Schema(description = "우대사항", example = "AWS 배포경험 있으신분")
    private final String preferred;

    @Schema(description = "기타조건", example = "포트폴리오 필수 제출")
    private final String etc;

    @Schema(description = "상세설명", example = "의뢰내용 : ----- / 지원자격 : ----")
    private final String description;

    public static WorkDetailResDto fromEntity(Work work) {
        User user = work.getRequester();
        return WorkDetailResDto.builder()
                .taskId(work.getId())
                .title(work.getTitle())
                .taskDuration(work.getTaskDuration())
                .price(work.getPrice())
                .eMail(user.getEMail())                // User 엔티티에서 이메일 가져오기
                .phoneNumber(user.getPhoneNumber())    // User 엔티티에서 전화번호 가져오기
                .headcount(work.getHeadCount())
                .academicBackground(work.getAcademicBackGround())
                .preferred(work.getPreferred())
                .etc(work.getEtc())
                .description(work.getDescription())
                .build();
    }


}
