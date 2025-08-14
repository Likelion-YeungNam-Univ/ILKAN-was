package com.ilkan.dto.reservationdto;

import com.ilkan.domain.entity.ReservationDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OccupiedDayResponseDto {
    private Long buildingId;
    private LocalDate day;

    public static OccupiedDayResponseDto fromEntity(ReservationDay d) {
        return OccupiedDayResponseDto.builder()
                .buildingId(d.getBuildingId())
                .day(d.getDay())
                .build();
    }

    public static List<OccupiedDayResponseDto> fromEntities(List<ReservationDay> list) {
        return list.stream().map(OccupiedDayResponseDto::fromEntity).toList();
    }
}
