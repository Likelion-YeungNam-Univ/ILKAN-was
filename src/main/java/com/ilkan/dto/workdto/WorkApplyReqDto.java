package com.ilkan.dto.workdto;

import com.ilkan.domain.entity.TaskApplication;
import com.ilkan.domain.entity.User;
import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "수행자 일거리 지원DTO")
public class WorkApplyReqDto {

    private final String bio;
    private final String portfolioUrl;

    public TaskApplication toEntity(Work work, User performer) {
        return TaskApplication.builder()
                .taskId(work)
                .performerId(performer)
                .bio(this.bio)
                .portfolioUrl(this.portfolioUrl)
                .status(Status.APPLY_TO)
                .appliedAt(java.time.LocalDateTime.now())
                .build();
    }
}
