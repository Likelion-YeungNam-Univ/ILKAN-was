package com.ilkan.domain.reservation.repository;

import com.ilkan.domain.reservation.entity.Reservation;
import com.ilkan.domain.reservation.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByReservationStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(
            ReservationStatus reservationStatus, LocalDateTime now1, LocalDateTime now2);

    List<Reservation> findAllByReservationStatusInAndEndTimeLessThanEqual(
            Collection<ReservationStatus> statuses, LocalDateTime now);

    boolean existsByBuildingId_Id(Long buildingId);

    long countByBuildingId_Id(Long buildingId);

}
