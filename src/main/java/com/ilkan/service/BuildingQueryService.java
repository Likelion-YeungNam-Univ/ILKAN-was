package com.ilkan.service;

import com.ilkan.domain.entity.Building;
import com.ilkan.domain.enums.BuildingTag;
import com.ilkan.domain.enums.Region;
import com.ilkan.dto.buildingdto.BuildingCardResDto;
import com.ilkan.dto.buildingdto.BuildingDetailResDto;
import com.ilkan.exception.BuildingQueryExceptions;
import com.ilkan.repository.BuildingRepository;
import com.ilkan.repository.BuildingSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuildingQueryService {

    private final BuildingRepository repo;

    /**
     * 빌딩 목록을 region/tag 조건과 페이지네이션으로 조회
     *
     * @param region   지역 필터(선택)
     * @param tag      태그 필터(선택)
     * @param pageable 페이지·정렬 정보(예: page, size, Sort)
     * @return 조회 결과 페이지(카드 DTO)
     * @throws BuildingQueryExceptions.DbDataCorrupted 엔티티-열거형 매핑 등 데이터 무결성 문제
     * @throws BuildingQueryExceptions.DbError         DB 접근/쿼리 수행 중 오류
     */

    public Page<BuildingCardResDto> search(Region region,
                                           BuildingTag tag,
                                           Pageable pageable) {
        try {
            Specification<Building> spec = Specification
                    .where(BuildingSpecs.regionEq(region))
                    .and(BuildingSpecs.tagEq(tag));

            return repo.findAll(spec, pageable).map(BuildingCardResDto::fromEntity);
        } catch (IllegalArgumentException e) {
            throw new BuildingQueryExceptions.DbDataCorrupted(e.getMessage());
        } catch (DataAccessException e) {
            e.getMostSpecificCause();
            throw new BuildingQueryExceptions.DbError(e.getMostSpecificCause().getMessage());
        }
    }

    /**
     * 건물 ID로 상세 정보를 조회
     *
     * @param id 건물 id (1 이상)
     * @return 상세 응답 DTO
     * @throws BuildingQueryExceptions.InvalidId
     *         잘못된 id 값(1 미만 또는 null)
     * @throws BuildingQueryExceptions.NotFound
     *         해당 id의 건물이 존재하지 않을 때
     * @throws BuildingQueryExceptions.DbDataCorrupted
     *         잘못된 enum/데이터 무결성 오류
     * @throws BuildingQueryExceptions.DbError
     *         DB 접근/쿼리 오류
     */

    public BuildingDetailResDto detail(Long id) {

        if (id == null || id < 1L) {
            throw new BuildingQueryExceptions.InvalidId(id);
        }

        try {
            Building b = repo.findById(id)
                    .orElseThrow(() -> new BuildingQueryExceptions.NotFound(id));

            String label = BuildingTag.toLabel(b.getBuildingTag()); // enum → 한글 라벨
            return BuildingDetailResDto.fromEntity(b, label);

        } catch (IllegalArgumentException e) {
            throw new BuildingQueryExceptions.DbDataCorrupted(e.getMessage());
        } catch (DataAccessException e) {
            throw new BuildingQueryExceptions.DbError(e.getMostSpecificCause().getMessage());
        }
    }
}
