package com.ilkan.domain.reservation.repository;

import com.ilkan.domain.reservation.entity.ReservationDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDayRepository extends JpaRepository<ReservationDay, Long> {

    // 사전 체크용
    boolean existsByBuildingIdAndDayBetween(Long buildingId, LocalDate from, LocalDate to);

    void deleteByReservationId(Long reservationId);

    // 점유일 달력 채우기
    List<ReservationDay> findByBuildingIdAndDayBetweenOrderByDayAsc(
            Long buildingId, LocalDate from, LocalDate to
    );

}
