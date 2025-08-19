package com.ilkan.controller.api;

import com.ilkan.dto.buildingdto.BuildingCardResDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(name = "BuildingCardPage", description = "공간 카드 페이징 응답 (Spring Page 직렬화 형태)")
public class BuildingCardPage {

    @ArraySchema(arraySchema = @Schema(description = "카드 목록"))
    private List<BuildingCardResDto> content;

    @Schema(description = "현재 페이지 번호(0-base)", example = "1") private int number;
    @Schema(description = "페이지 크기", example = "15") private int size;
    @Schema(description = "전체 요소 수", example = "24") private long totalElements;
    @Schema(description = "전체 페이지 수", example = "2") private int totalPages;
    @Schema(description = "첫 페이지 여부", example = "false") private boolean first;
    @Schema(description = "마지막 페이지 여부", example = "true") private boolean last;
    @Schema(description = "페이지 내 요소 수", example = "9") private int numberOfElements;
    @Schema(description = "비었는지 여부", example = "false") private boolean empty;

    @Schema(description = "정렬 정보") private SortInfo sort;
    @Schema(description = "pageable 정보") private PageableInfo pageable;

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    @Schema(name = "SortInfo")
    public static class SortInfo {
        @Schema(example = "false") private boolean empty;
        @Schema(example = "true") private boolean sorted;
        @Schema(example = "false") private boolean unsorted;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    @Schema(name = "PageableInfo")
    public static class PageableInfo {
        @Schema(example = "1") private int pageNumber;
        @Schema(example = "15") private int pageSize;
        @Schema(description = "정렬 정보") private SortInfo sort;
        @Schema(example = "15") private long offset;
        @Schema(example = "true") private boolean paged;
        @Schema(example = "false") private boolean unpaged;
    }
}
