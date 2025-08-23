package com.ilkan.domain.building.service;

import com.ilkan.domain.building.entity.Building;
import com.ilkan.domain.file.service.FileStorageService;
import com.ilkan.domain.profile.entity.User;
import com.ilkan.domain.building.entity.enums.BuildingTag;
import com.ilkan.domain.building.entity.enums.Region;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.building.dto.BuildingCreateReq;
import com.ilkan.domain.building.dto.BuildingCreateRes;
import com.ilkan.exception.BuildingCommandExceptions;
import com.ilkan.exception.RoleExceptions;
import com.ilkan.domain.building.repository.BuildingRepository;
import com.ilkan.domain.reservation.repository.ReservationRepository;
import com.ilkan.domain.auth.repository.UserRepository;
import com.ilkan.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;
    private final ReservationRepository reservationRepository;
    private final FileStorageService fileStorageService;

    /**
     * 건물주(OWNER) 역할의 유저가 새로운 건물을 등록합니다.
     *
     * @param roleHeader 요청 헤더의 역할 문자열 (예: "OWNER")
     * @param req 건물 등록 요청 DTO
     * @return 등록된 건물의 응답 DTO
     *
     * @throws RoleExceptions.Missing X-Role 헤더가 없는 경우
     * @throws RoleExceptions.Invalid 유효하지 않은 역할이 전달된 경우
     * @throws RoleExceptions.NotFound 해당 역할의 유저가 존재하지 않는 경우
     * @throws BuildingCommandExceptions.InvalidRegion 잘못된 지역 값인 경우
     * @throws BuildingCommandExceptions.InvalidTag 잘못된 태그 값인 경우
     * @throws BuildingCommandExceptions.DuplicatedName 동일 소유자의 건물명이 중복된 경우
     */
    @Transactional
    public BuildingCreateRes createBuilding(String roleHeader, BuildingCreateReq req, MultipartFile mainImage, MultipartFile subImage1, MultipartFile subImage2) {

        if (roleHeader == null || roleHeader.isBlank()) throw new RoleExceptions.Missing();
        if (mainImage == null || mainImage.isEmpty()) {
            throw new IllegalArgumentException("대표 이미지는 필수입니다.");
        }

        final Role role;
        try { role = Role.valueOf(roleHeader.trim().toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException e) { throw new RoleExceptions.Invalid(roleHeader); }

        Long ownerId = RoleMapper.getUserIdByRole(role.name());
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RoleExceptions.NotFound(role));

        Region region = parseEnumOrThrow(req.getBuildingRegion(), Region.class,
                () -> new BuildingCommandExceptions.InvalidRegion(req.getBuildingRegion()));
        BuildingTag tag = parseEnumOrThrow(req.getBuildingTag(), BuildingTag.class,
                () -> new BuildingCommandExceptions.InvalidTag(req.getBuildingTag()));

        // 동일 소유자 -> 동일한 빌딩 이름 금지
        if (buildingRepository.existsByOwnerAndBuildingName(owner, req.getBuildingName())) {
            throw new BuildingCommandExceptions.DuplicatedName(req.getBuildingName());
        }

        Building entity = req.toEntity(owner, region, tag);

        String keyMain = uploadNullable(ownerId, mainImage);
        String keySub1 = uploadNullable(ownerId, subImage1);
        String keySub2 = uploadNullable(ownerId, subImage2);

        entity.updateBuildingImage(keyMain);
        if (keySub1 != null) entity.updateBuildingImage1(keySub1);
        if (keySub2 != null) entity.updateBuildingImage2(keySub2);

        Building saved = buildingRepository.save(entity);

        return BuildingCreateRes.fromEntity(saved);
    }

    private String uploadNullable(Long ownerId, MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            FileStorageService.UploadResult r = fileStorageService.upload(ownerId, file);
            return r.key();
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

    private String toUrl(String key) {
        return key == null ? null : fileStorageService.getFileUrl(key);
    }

    // 문자열 -> enum
    private <E extends Enum<E>> E parseEnumOrThrow(String raw, Class<E> type, SupplierX ex) {
        try { return Enum.valueOf(type, raw.trim().toUpperCase(Locale.ROOT)); }
        catch (Exception e) { throw ex.get(); }
    }
    @FunctionalInterface private interface SupplierX { RuntimeException get(); }

    /**
     * 건물 하드 삭제. 건물에 예약이 1건이라도 존재하면 409로 차단.
     *
     * @throws RoleExceptions.Missing X-Role 헤더 없음
     * @throws RoleExceptions.Invalid 알 수 없는 역할 문자열
     * @throws BuildingCommandExceptions.NotFound 건물이 없을 때
     * @throws BuildingCommandExceptions.DeleteBlockedByReservations 예약 존재 시
     */
    @Transactional
    public void deleteBuilding(String roleHeader, Long buildingId) {
        if (roleHeader == null || roleHeader.isBlank()) throw new RoleExceptions.Missing();
        final Role role;
        try { role = Role.valueOf(roleHeader.trim().toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException e) { throw new RoleExceptions.Invalid(roleHeader); }

        var building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new BuildingCommandExceptions.NotFound(buildingId));

        // 예약이 1건이라도 있으면 차단
        if (reservationRepository.existsByBuildingId_Id(buildingId)) {
            Long cnt = null;
            try { cnt = reservationRepository.countByBuildingId_Id(buildingId); } catch (Exception ignore) {}
            throw new BuildingCommandExceptions.DeleteBlockedByReservations(buildingId, cnt);
        }

        buildingRepository.delete(building);
    }
}
