package com.ilkan.domain.entity;

import com.ilkan.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "work")
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 자동 증가
    @Column(name = "task_id")
    private Long id;

    // 의뢰자 정보 (User와 다대일 관계 , 한명이 여러 일거리 등록)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String description; // 상세설명

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 등록시간

    @Column
    private Long price; // 가격

    @Column(name = "is_negotiable")
    private Boolean isNegotiable; // 금액 협의 가능여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // 일거리 상태

    @Column(name = "task_start", nullable = false)
    private LocalDateTime taskStart; // 일거리 시작시간

    @Column(name = "task_end", nullable = false)
    private LocalDateTime taskEnd; // 일거리 마감시간

    // ==== 변경 메서드 ====
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updatePrice(Long price) {
        if (price != null && price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
        this.price = price;
    }

    public void updateIsNegotiable(Boolean isNegotiable) {
        this.isNegotiable = isNegotiable;
    }

    public void updateStatus(Status status) {
        // 상태 변경 로직(예: 완료 → 진행중 불가) 검증 가능
        this.status = status;
    }

    public void updateTaskPeriod(LocalDateTime taskStart, LocalDateTime taskEnd) {
        if (taskStart.isAfter(taskEnd)) {
            throw new IllegalArgumentException("시작일은 마감일보다 이후일 수 없습니다.");
        }
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
    }
}
