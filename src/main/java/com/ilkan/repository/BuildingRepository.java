package com.ilkan.repository;

import com.ilkan.domain.entity.Building;
import com.ilkan.domain.enums.BuildingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Page<Building> findByOwner_IdAndBuildingStatus(Long ownerId, BuildingStatus buildingStatus, Pageable pageable); // 건물주 등록한 건물조회

    // 동시성 제어용 (예약 생성 시)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Building b where b.id = :id")
    Optional<Building> findForUpdate(@Param("id") Long id);


}
