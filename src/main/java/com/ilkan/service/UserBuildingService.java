package com.ilkan.service;

import com.ilkan.domain.entity.Building;
import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.BuildingStatus;
import com.ilkan.domain.enums.ReservationStatus;
import com.ilkan.dto.reservationdto.OwnerBuildingResDto;
import com.ilkan.dto.reservationdto.OwnersInUseResDto;
import com.ilkan.dto.reservationdto.UserBuildingResDto;
import com.ilkan.exception.UserBuildingExceptions;
import com.ilkan.repository.BuildingRepository;
import com.ilkan.repository.UserBuildingRepository;
import com.ilkan.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBuildingService {

    private final UserBuildingRepository userBuildingRepository;
    private final BuildingRepository buildingRepository;

    // 수행자가 사용중인 공간 조회
    @Transactional(readOnly = true)
    public Page<UserBuildingResDto> findUsingBuildingsByPerformer(String roleHeader, Pageable pageable) {
        if (!"PERFORMER".equals(roleHeader)) {
            throw new UserBuildingExceptions.PerformerForbidden();
        }
        Long performerId = RoleMapper.getUserIdByRole(roleHeader);
        Page<Reservation> reservations = userBuildingRepository.findByPerformerId_IdAndReservationStatus(performerId, ReservationStatus.IN_USE, pageable);
        if (reservations.isEmpty()) { // 조회 결과가 없으면 예외
            throw new UserBuildingExceptions.UserBuildingNotFound();
        }
        return reservations.map(UserBuildingResDto::fromEntity);
    }

    // 건물주가 등록한 건물 조회 - 페이지 가장 아래 기능
    @Transactional(readOnly = true)
    public Page<OwnerBuildingResDto> getRegisteredBuildings(String roleHeader, Pageable pageable) {
        if (!"OWNER".equals(roleHeader)) {
            throw new UserBuildingExceptions.OwnerForbidden();
        }
        Long ownerId = RoleMapper.getUserIdByRole(roleHeader);
        Page<Building> buildings = buildingRepository.findByOwner_IdAndBuildingStatus(ownerId, BuildingStatus.REGISTERED, pageable);
        if (buildings.isEmpty()) { // 조회 결과가 없으면 예외
            throw new UserBuildingExceptions.OwnerBuildingNotFound();
        }
        return buildings.map(OwnerBuildingResDto::fromEntity);
    }

    // 건물주가 등록한 건물 중 사용중인 건물 조회 - 페이지 가장 윗 기능
    @Transactional(readOnly = true)
    public Page<OwnersInUseResDto> getBuildingsInUse(String roleHeader, Pageable pageable) {
        if (!"OWNER".equals(roleHeader)) {
            throw new UserBuildingExceptions.OwnerForbidden();
        }

        Long ownerId = RoleMapper.getUserIdByRole(roleHeader);
        Page<Reservation> reservationsInUse = userBuildingRepository
                .findByBuildingId_Owner_IdAndReservationStatus(ownerId, ReservationStatus.IN_USE, pageable);

        if (reservationsInUse.isEmpty()) {
            throw new UserBuildingExceptions.OwnerBuildingNotFound();
        }
        return reservationsInUse.map(OwnersInUseResDto::fromEntity);
    }


}

