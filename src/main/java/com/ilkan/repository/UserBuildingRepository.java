package com.ilkan.repository;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBuildingRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByPerformerId_IdAndReservationStatus(
            Long performerId, ReservationStatus status, Pageable pageable);

    Page<Reservation> findByBuildingId_Owner_IdAndReservationStatus(Long ownerId, ReservationStatus reservationStatus, Pageable pageable); // 건물주가 등록한 건물 중 사용중인것 조회

}
