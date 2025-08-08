package com.ilkan.domain.entity;

import com.ilkan.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "work")
public class Work {

    @Id
    @Column(name = "task_id")
    private Long id;

    // 의뢰자 정보 (User와 다대일 관계 , 한명이 여러 일거리 등록)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id" , nullable = false)
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // 일거리 상태

    @Column(name = "task_start" , nullable = false)
    private LocalDateTime taskStart; // 일거리 시작시간
    @Column(name = "task_end" , nullable = false)
    private LocalDateTime taskEnd; // 일거리 마감시간

}