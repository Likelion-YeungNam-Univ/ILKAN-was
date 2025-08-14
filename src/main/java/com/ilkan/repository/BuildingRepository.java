package com.ilkan.repository;

import com.ilkan.domain.entity.Building;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    // 동시성 제어용 (예약 생성 시)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Building b where b.id = :id")
    Optional<Building> findForUpdate(@Param("id") Long id);
}
