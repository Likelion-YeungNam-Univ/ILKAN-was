package com.ilkan.dto.workdto;

import com.ilkan.domain.entity.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import com.ilkan.domain.enums.Status;
import com.ilkan.domain.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class WorkRequestDto {

    private final Long id;
    private final Long requesterId;
    private final String title;
    private final String description;
    private final Long price;
    private final Boolean isNegotiable;
    private final Status status;
    private final LocalDateTime taskStart;
    private final LocalDateTime taskEnd;

    // DB에 저장할 엔티티 객체 생성하기 위함
    public Work toEntity(User requester) {
        return Work.builder()
                .id(this.id)
                .requester(requester)
                .title(this.title)
                .description(this.description)
                .createdAt(LocalDateTime.now())
                .price(this.price)
                .isNegotiable(this.isNegotiable)
                .status(this.status)
                .taskStart(this.taskStart)
                .taskEnd(this.taskEnd)
                .build();
    }
}

