package com.ilkan.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="reservation_day",
        uniqueConstraints=@UniqueConstraint(name="uq_building_day", columnNames={"building_id","day"}))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReservationDay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="reservation_id", nullable=false)
    private Long reservationId;

    @Column(name="building_id", nullable=false)
    private Long buildingId;

    @Column(nullable=false)
    private LocalDate day;
}
