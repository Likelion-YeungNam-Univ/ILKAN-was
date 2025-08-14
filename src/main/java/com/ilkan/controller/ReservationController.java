package com.ilkan.controller;

import com.ilkan.auth.AllowedRoles;
import com.ilkan.domain.enums.Role;
import com.ilkan.dto.reservationdto.CreateReservationRequestDto;
import com.ilkan.dto.reservationdto.OccupiedDayResponseDto;
import com.ilkan.dto.reservationdto.ReservationResponseDto;
import com.ilkan.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReservationController {

    private final ReservationService reservationService;

    // 수행자 건물 예약
    @AllowedRoles(Role.PERFORMER)
    @PostMapping("/reservations")
    public ReservationResponseDto create(@RequestBody CreateReservationRequestDto req, @RequestHeader("X-Role") Role role) {
        return reservationService.create(req, role);
    }

    // 예약 취소
    @AllowedRoles({Role.PERFORMER, Role.OWNER})
    @PostMapping("/reservations/{id}/cancel")
    public void cancel(@PathVariable Long id,
                       @RequestHeader("X-Role") Role role) {
        reservationService.cancel(id, role);
    }

    // 점유일
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/reservations/{buildingId}/occupied-days")
    public List<OccupiedDayResponseDto> occupiedDays(
            @PathVariable Long buildingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reservationService.getOccupiedDays(buildingId, from, to);
    }
}
