package com.ilkan.domain.entity;

import com.ilkan.domain.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id")
    private User performerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building buildingId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus reservationStatus;

    // ==== 변경 메서드 ====
    public void updatePerformer(User performerId) {
        this.performerId = performerId;
    }

    public void updatePhoneNumber(Building buildingId) {
        this.buildingId = buildingId;
    }

    public void updateRole(LocalDateTime startTime) { this.startTime = startTime; }

    public void updateProfileImage(LocalDateTime endTime) { this.endTime = endTime; }

    public void toReserved() {
        this.reservationStatus = ReservationStatus.RESERVED;
    }
    public void toInUse() {
        if (reservationStatus == ReservationStatus.CANCELED || reservationStatus == ReservationStatus.COMPLETE) return;
        this.reservationStatus = ReservationStatus.IN_USE;
    }
    public void toComplete() {
        if (reservationStatus == ReservationStatus.CANCELED) return;
        this.reservationStatus = ReservationStatus.COMPLETE;
    }
    public void cancel() {
        if (reservationStatus == ReservationStatus.COMPLETE) return; // 필요 시 규칙 조정
        this.reservationStatus = ReservationStatus.CANCELED;
    }

}
