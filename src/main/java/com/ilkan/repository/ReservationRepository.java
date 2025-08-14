package com.ilkan.repository;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
      select r from Reservation r
      where r.reservationStatus = com.ilkan.domain.enums.ReservationStatus.RESERVED
        and r.startTime <= :now and r.endTime > :now
    """)
    List<Reservation> findToInUse(@Param("now") LocalDateTime now);

    @Query("""
      select r from Reservation r
      where r.reservationStatus in (com.ilkan.domain.enums.ReservationStatus.RESERVED,
                                    com.ilkan.domain.enums.ReservationStatus.IN_USE)
        and r.endTime <= :now
    """)
    List<Reservation> findToComplete(@Param("now") LocalDateTime now);
}
