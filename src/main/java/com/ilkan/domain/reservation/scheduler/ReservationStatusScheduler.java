package com.ilkan.domain.reservation.scheduler;

import com.ilkan.domain.reservation.entity.Reservation;
import com.ilkan.domain.reservation.entity.enums.ReservationStatus;
import com.ilkan.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationStatusScheduler {

    private final ReservationRepository reservationRepo;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    @Transactional
    public void tick() {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneOffset.UTC);

        // RESERVED → IN_USE
        var toInUse = reservationRepo
                .findAllByReservationStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(
                        ReservationStatus.RESERVED, now, now);
        toInUse.forEach(Reservation::toInUse);


        // RESERVED/IN_USE → COMPLETE
        var toComplete = reservationRepo
                .findAllByReservationStatusInAndEndTimeLessThanEqual(
                        List.of(ReservationStatus.RESERVED, ReservationStatus.IN_USE), now);
        toComplete.forEach(Reservation::toComplete);
    }
}