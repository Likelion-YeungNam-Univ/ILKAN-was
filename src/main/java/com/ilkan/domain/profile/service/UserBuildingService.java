package com.ilkan.domain.profile.service;

import com.ilkan.domain.building.entity.Building;
import com.ilkan.domain.building.repository.BuildingRepository;
import com.ilkan.domain.profile.dto.owner.OwnerBuildingResDto;
import com.ilkan.domain.profile.dto.owner.OwnersInUseResDto;
import com.ilkan.domain.profile.dto.owner.OwnersReservedResDto;
import com.ilkan.domain.profile.dto.performer.UserBuildingResDto;
import com.ilkan.domain.profile.repository.UserBuildingRepository;
import com.ilkan.domain.reservation.entity.Reservation;
import com.ilkan.domain.reservation.entity.enums.ReservationStatus;
import com.ilkan.exception.UserBuildingExceptions;
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

        return reservations.map(UserBuildingResDto::fromEntity);
    }

    // 건물주가 등록한 건물 조회 - 페이지 가장 아래 기능
    @Transactional(readOnly = true)
    public Page<OwnerBuildingResDto> getRegisteredBuildings(String roleHeader, Pageable pageable) {
        if (!"OWNER".equals(roleHeader)) {
            throw new UserBuildingExceptions.OwnerForbidden();
        }
        Long ownerId = RoleMapper.getUserIdByRole(roleHeader);
        Page<Building> buildings = buildingRepository.findByOwner_Id(ownerId, pageable);

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


        return reservationsInUse.map(OwnersInUseResDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<OwnersReservedResDto> getReservedBuildings(String roleHeader, Pageable pageable) {
        if (!"OWNER".equals(roleHeader)) {
            throw new UserBuildingExceptions.OwnerForbidden();
        }

        Long ownerId = RoleMapper.getUserIdByRole(roleHeader);
        Page<Reservation> reservedReservations = userBuildingRepository
                .findByBuildingId_Owner_IdAndReservationStatus(ownerId, ReservationStatus.RESERVED, pageable);

        return reservedReservations.map(OwnersReservedResDto::fromEntity);
    }




}

