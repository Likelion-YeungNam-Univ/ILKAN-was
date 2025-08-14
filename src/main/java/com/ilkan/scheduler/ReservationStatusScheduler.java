package com.ilkan.scheduler;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.repository.ReservationRepository;
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
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 1. IN_USE인 예약 목록 조회
        List<Reservation> toInUse = reservationRepo.findToInUse(now);
        toInUse.forEach(Reservation::toInUse);

        // 2. COMPLETE인 예약 목록 조회
        List<Reservation> toComplete = reservationRepo.findToComplete(now);
        toComplete.forEach(Reservation::toComplete);
    }
}
