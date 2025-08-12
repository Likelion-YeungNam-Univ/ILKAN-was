package com.ilkan.service;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.BuildingStatus;
import com.ilkan.dto.reservationdto.UserBuildingResDto;
import com.ilkan.repository.UserBuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBuildingService {
    // (수행자) 내가 사용중인 일칸조회
    private final UserBuildingRepository userBuildingRepository;

    public Page<UserBuildingResDto> getBuildingsByPerformerAndStatus(Long performerId, BuildingStatus status, Pageable pageable) {
        Page<Reservation> reservations = userBuildingRepository.findByPerformerIdAndBuildingStatus(performerId, status, pageable);
        return reservations.map(UserBuildingResDto::fromEntity);
    }
}
