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

    @Column(nullable = false)
    private Boolean isAccepted; // 이용 승인여부

    @Enumerated(EnumType.STRING)
    @Column(name = "building_status", nullable = false)
    private ReservationStatus buildingStatus;

    // ==== 변경 메서드 ====
    public void updatePerformer(User performerId) {
        this.performerId = performerId;
    }

    public void updatePhoneNumber(Building buildingId) {
        this.buildingId = buildingId;
    }

    public void updateRole(LocalDateTime startTime) { this.startTime = startTime; }

    public void updateProfileImage(LocalDateTime endTime) { this.endTime = endTime; }

    public void updateBioBoolean (Boolean isAccepted) { this.isAccepted = isAccepted; }

}
