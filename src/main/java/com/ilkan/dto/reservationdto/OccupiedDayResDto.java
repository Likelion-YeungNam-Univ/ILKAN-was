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
public class OccupiedDayResDto {
    private Long buildingId;
    private LocalDate day;

    public static OccupiedDayResDto fromEntity(ReservationDay d) {
        return OccupiedDayResDto.builder()
                .buildingId(d.getBuildingId())
                .day(d.getDay())
                .build();
    }

    public static List<OccupiedDayResDto> fromEntities(List<ReservationDay> list) {
        return list.stream().map(OccupiedDayResDto::fromEntity).toList();
    }
}
