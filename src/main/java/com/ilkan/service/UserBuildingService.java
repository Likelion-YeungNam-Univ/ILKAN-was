package com.ilkan.service;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.ReservationStatus;
import com.ilkan.dto.reservationdto.OwnerBuildingResDto;
import com.ilkan.dto.reservationdto.UserBuildingResDto;
import com.ilkan.exception.UserBuildingExceptions;
import com.ilkan.repository.UserBuildingRepository;
import com.ilkan.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBuildingService {

    private final UserBuildingRepository userBuildingRepository;

    // 수행자가 사용중인 공간 조회
    public Page<UserBuildingResDto> findUsingBuildingsByPerformer(String role, Pageable pageable) {
        if (!"PERFORMER".equals(role)) {
            throw new UserBuildingExceptions.PerformerForbidden();
        }
        Long performerId = RoleMapper.getUserIdByRole(role);
        Page<Reservation> reservations = userBuildingRepository.findByPerformerId_IdAndReservationStatus(performerId, ReservationStatus.IN_USE, pageable);
        if (reservations.isEmpty()) { // 조회 결과가 없으면 예외
            throw new UserBuildingExceptions.UserBuildingNotFound();
        }
        return reservations.map(UserBuildingResDto::fromEntity);
    }

    // 건물주가 등록한 건물 조회
    public Page<OwnerBuildingResDto> getRegisteredBuildings(String role, Pageable pageable) {
        if (!"OWNER".equals(role)) {
            throw new UserBuildingExceptions.OwnerForbidden();
        }
        Long ownerId = RoleMapper.getUserIdByRole(role);
        Page<Reservation> buildings = userBuildingRepository.findByBuildingId_Owner_IdAndReservationStatus(ownerId, ReservationStatus.REGISTERED, pageable);
        if (buildings.isEmpty()) { // 조회 결과가 없으면 예외
            throw new UserBuildingExceptions.OwnerBuildingNotFound();
        }
        return buildings.map(OwnerBuildingResDto::fromEntity);
    }

}

