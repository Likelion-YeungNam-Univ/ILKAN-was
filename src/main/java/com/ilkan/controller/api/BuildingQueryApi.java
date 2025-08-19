package com.ilkan.controller.api;

import com.ilkan.domain.enums.BuildingTag;
import com.ilkan.domain.enums.Region;
import com.ilkan.dto.buildingdto.BuildingCardRespDto;
import com.ilkan.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "BuildingQuery", description = "공간(건물) 목록 조회 API")
@RequestMapping(value = "/api/v1/buildings", produces = "application/json")
public interface BuildingQueryApi {

    @Operation(
            summary = "공간 목록 조회",
            description = """
            공간 카드 리스트를 페이지네이션하여 반환합니다.
            - 정렬: 최신순(id desc) 고정
            - page는 0부터 시작
            - size 기본값: 15 (허용: 1~50)
            - region/tag는 선택값(없으면 전체)
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BuildingCardPage.class),
                            examples = @ExampleObject(name = "SUCCESS", value = """
                {
                  "totalElements": 24,
                  "totalPages": 2,
                  "size": 15,
                  "content": [
                    {
                      "id": 21,
                      "owner": "의뢰자",
                      "buildingImage": "https://cdn.example.com/buildings/os-005.jpg",
                      "buildingPrice": 65000,
                      "region": "GYEONGGI",
                      "tag": "OFFICE_SPACE",
                      "buildingName": "분당 공유오피스"
                    }
                  ],
                  "number": 1,
                  "sort": { "empty": false, "sorted": true, "unsorted": false },
                  "first": false,
                  "last": true,
                  "numberOfElements": 9,
                  "pageable": {
                    "pageNumber": 1,
                    "pageSize": 15,
                    "sort": { "empty": false, "sorted": true, "unsorted": false },
                    "offset": 15, "paged": true, "unpaged": false
                  },
                  "empty": false
                }""")
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "파라미터 오류(page/size 범위, enum 형식 등)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name="BUILDING_INVALID_PAGE", value = """
                    { "code":"BUILDING_INVALID_PAGE","message":"page는 0 이상이어야 합니다.","status":400,
                      "path":"/api/v1/buildings","timestamp":"2025-08-19T10:00:00" }"""),
                                    @ExampleObject(name="BUILDING_INVALID_SIZE", value = """
                    { "code":"BUILDING_INVALID_SIZE","message":"size는 1이상, 50이하여야 합니다.","status":400,
                      "path":"/api/v1/buildings","timestamp":"2025-08-19T10:00:00" }"""),
                                    @ExampleObject(name="INVALID_ENUM", value = """
                    { "code":"INVALID_ENUM","message":"파라미터 'region' 값 'ABC'은(는) 허용되지 않습니다. 허용값: [SEOUL, BUSAN, GYEONGGI, ...]","status":400,
                      "path":"/api/v1/buildings","timestamp":"2025-08-19T10:00:00" }""")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "DB 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name="BUILDING_DB_ERROR", value = """
                    { "code":"BUILDING_DB_ERROR","message":"database error","status":500,
                      "path":"/api/v1/buildings","timestamp":"2025-08-19T10:00:00" }"""),
                                    @ExampleObject(name="BUILDING_DB_DATA_CORRUPTED", value = """
                    { "code":"BUILDING_DB_DATA_CORRUPTED","message":"building_region 컬럼에 잘못된 값이 있습니다.","status":500,
                      "path":"/api/v1/buildings","timestamp":"2025-08-19T10:00:00" }""")
                            }
                    )
            )
    })
    @GetMapping
    Page<BuildingCardRespDto> list(
            @Parameter(description = "지역(시/도) 필터", required = false,
                    schema = @Schema(implementation = Region.class), example = "SEOUL")
            @RequestParam(required = false) Region region,

            @Parameter(description = "태그 필터", required = false,
                    schema = @Schema(implementation = BuildingTag.class), example = "OFFICE_SPACE")
            @RequestParam(required = false) BuildingTag tag,

            @Parameter(description = "페이지 번호(0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기(기본 15, 허용 1~50)", example = "15")
            @RequestParam(required = false) Integer size
    );
}
