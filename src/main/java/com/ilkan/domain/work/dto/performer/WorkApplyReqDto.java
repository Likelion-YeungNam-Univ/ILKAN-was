package com.ilkan.domain.work.dto.performer;

import com.ilkan.domain.work.entity.TaskApplication;
import com.ilkan.domain.profile.entity.User;
import com.ilkan.domain.work.entity.Work;
import com.ilkan.domain.work.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "수행자 일거리 지원DTO")
public class WorkApplyReqDto {

    @Schema(description = "자기소개", example = "안녕하세요 저는~~")
    private final String bio;

    @Schema(description = "포트폴리오 URL", example = "https://~~~")
    private final String portfolioUrl;

    public TaskApplication toEntity(Work work, User performer) {
        return TaskApplication.builder()
                .taskId(work)
                .performerId(performer)
                .requesterId(work.getRequester())
                .bio(this.bio)
                .portfolioUrl(this.portfolioUrl)
                .status(Status.APPLY_TO)
                .appliedAt(java.time.LocalDateTime.now())
                .build();
    }
}
