package com.ilkan.domain.entity;

import com.ilkan.domain.enums.Status;
import com.ilkan.domain.enums.WorkCategory;
import com.ilkan.dto.workdto.WorkReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 기본생성자로 무의미한 , 불완전한 객체 생성을 방지
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

    // 수행자 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id")
    private User performer;

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String description; // 상세설명

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 등록시간

    @Column
    private Long price; // 가격

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // 일거리 상태

    @Column(name = "task_start")
    private LocalDateTime taskStart; // 일거리 시작시간

    @Column(name = "task_end")
    private LocalDateTime taskEnd; // 일거리 마감시간

    @Column(name = "task_duration", nullable = false)
    private String taskDuration; // 일거리 작업기간

    @Column
    private Long headCount; // 모집인원

    @Column
    private String academicBackGround; // 학력

    @Column
    private String preferred; // 우대사항

    @Column
    private String etc; // 기타조건

    @Column
    private LocalDateTime recruitmentPeriod; // 모집 기한

    @Column
    private String workEmail; // 일거리등록시 사용 이메일

    @Column
    private String workPhoneNumber; // 일거리등록시 사용 번호

    @Enumerated(EnumType.STRING)
    @Column
    private WorkCategory workCategory;

    // ==== 변경 메서드 ====
    // 등록시간은 수정불가
    public void updateFromDto(WorkReqDto dto) {
        this.title = dto.getTitle();
        this.recruitmentPeriod = dto.getRecruitmentPeriod();
        this.taskDuration = dto.getTaskDuration();
        this.price = dto.getPrice();
        this.headCount = dto.getHeadCount();
        this.academicBackGround = dto.getAcademicBackground();
        this.preferred = dto.getPreferred();
        this.etc = dto.getEtc();
        this.description = dto.getDescription();
        this.workEmail = dto.getWorkEmail();
        this.workPhoneNumber = dto.getWorkPhoneNumber();
    }

    public void updatePerformer(User performer){this.performer = performer;}

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateTaskPeriod(LocalDateTime taskStart, LocalDateTime taskEnd) {
        if (taskStart.isAfter(taskEnd)) {
            throw new IllegalArgumentException("시작일은 마감일보다 이후일 수 없습니다.");
        }
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
    }

    public void updateWorkCategory(WorkCategory workCategory) {this.workCategory = workCategory;}


}
