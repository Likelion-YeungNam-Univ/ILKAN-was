package com.ilkan.domain.entity;

import com.ilkan.domain.enums.Status;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TaskApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long id; // 지원기록 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Work taskId; // 일거리 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id", nullable = false)
    private User performerId; // 수행자 id

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column
    private String bio; // 자기소개

    @Column
    private String portfolioUrl;

    public void updateStatus(Status status) {this.status = status;}

    public void updateAppliedAt(LocalDateTime appliedAt) {this.appliedAt = appliedAt;}

    public void updateMessage(String message) {this.bio = message;}

    public void updatePortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
}
