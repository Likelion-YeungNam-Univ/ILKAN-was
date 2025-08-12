package com.ilkan.domain.entity;

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
    private User performer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Boolean isAccepted; // 이용 승인여부

    // ==== 변경 메서드 ====
    public void updatePerformer(User performer) {
        this.performer = performer;
    }

    public void updatePhoneNumber(Building building) {
        this.building = building;
    }

    public void updateRole(LocalDateTime startTime) { this.startTime = startTime; }

    public void updateProfileImage(LocalDateTime endTime) { this.endTime = endTime; }

    public void updateBioBoolean (Boolean isAccepted) { this.isAccepted = isAccepted; }

}
