package com.ilkan.repository;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBuildingRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByPerformerId_IdAndReservationStatus(
            Long performerId, ReservationStatus status, Pageable pageable);

}
