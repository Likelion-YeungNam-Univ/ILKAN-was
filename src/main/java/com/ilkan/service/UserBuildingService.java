package com.ilkan.service;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.ReservationStatus;
import com.ilkan.dto.reservationdto.UserBuildingResDto;
import com.ilkan.repository.UserBuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBuildingService {

    private final UserBuildingRepository userBuildingRepository;

    // // role 기반 수행자가 사용중인 공간 조회
    public Page<UserBuildingResDto> findUsingBuildingsByPerformer(String role, Pageable pageable) {
        Long performerId = getUserIdByRole(role);
        Page<Reservation> reservations = userBuildingRepository.findByPerformerId_IdAndReservationStatus(performerId, ReservationStatus.IN_USE, pageable);
        return reservations.map(UserBuildingResDto::fromEntity);
    }

    // role → userId 매핑
    private Long getUserIdByRole(String role) {
        switch (role.toUpperCase()) {
            case "REQUESTER": return 1L;
            case "PERFORMER": return 2L;
            case "OWNER": return 3L;
            default: throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}

