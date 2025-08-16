package com.ilkan.dto.workdto;

import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import com.ilkan.dto.userdto.UserRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class WorkResDto {

    private final Long taskId;                     // 일거리 ID
    private final UserRespDto requester;   // 의뢰자 정보 (UserResponseDto로 매핑)
    private final String title;                 // 제목
    private final String description;           // 상세 설명
    private final LocalDateTime createdAt;      // 등록 시간
    private final Long price;                   // 가격
    private final Status status;                // 일거리 상태
    private final LocalDateTime taskStart;      // 작업 시작 시간
    private final LocalDateTime taskEnd;        // 작업 마감 시간
    private final LocalDateTime recruitmentPeriod;


    // DB에서 조회한 Entity를 API 응답용 DTO로 변환하기 위함
    public static WorkResDto fromEntity(Work work) {
        return WorkResDto.builder()
                .taskId(work.getId())
                .requester(UserRespDto.fromEntity(work.getRequester()))
                .title(work.getTitle())
                .description(work.getDescription())
                .createdAt(work.getCreatedAt())
                .price(work.getPrice())
                .status(work.getStatus())
                .taskStart(work.getTaskStart())
                .taskEnd(work.getTaskEnd())

                .build();
    }
}
