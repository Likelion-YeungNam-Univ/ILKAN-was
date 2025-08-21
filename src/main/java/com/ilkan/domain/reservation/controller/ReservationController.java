package com.ilkan.domain.reservation.controller;

import com.ilkan.security.AllowedRoles;
import com.ilkan.domain.reservation.api.ReservationApi;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.reservation.dto.CreateReservationReqDto;
import com.ilkan.domain.reservation.dto.OccupiedDayResDto;
import com.ilkan.domain.reservation.dto.ReservationResDto;
import com.ilkan.domain.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Reservation", description = "건물 예약/취소/점유일 API")
public class ReservationController implements ReservationApi {

    private final ReservationService reservationService;

    // 1. 수행자 건물 예약
    @AllowedRoles(Role.PERFORMER)
    @PostMapping("/reservations")
    public ReservationResDto create(@RequestBody CreateReservationReqDto req, @RequestHeader("X-Role") Role role) {
        return reservationService.create(req, role);
    }

    // 2. 예약 취소
    @AllowedRoles({Role.PERFORMER, Role.OWNER})
    @PostMapping("/reservations/{id}/cancel")
    public void cancel(@PathVariable Long id,
                       @RequestHeader("X-Role") Role role) {
        reservationService.cancel(id, role);
    }

    // 3. 점유일
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/reservations/{buildingId}/occupied-days")
    public List<OccupiedDayResDto> occupiedDays(
            @PathVariable Long buildingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reservationService.getOccupiedDays(buildingId, from, to);
    }
}
