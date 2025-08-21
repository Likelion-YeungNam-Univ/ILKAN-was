package com.ilkan.domain.reservation.api;

import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.reservation.dto.CreateReservationReqDto;
import com.ilkan.domain.reservation.dto.OccupiedDayResDto;
import com.ilkan.domain.reservation.dto.ReservationResDto;
import com.ilkan.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Reservation", description = "건물 예약/취소/점유일 API")
@RequestMapping(value = "/api/v1", produces = "application/json")
public interface ReservationApi {

    // 1) 예약 생성
    @Operation(summary = "예약 생성", description = "수행자(PERFORMER)가 건물 예약 생성")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "예약 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservationResDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "reservationId": 1,
                                    "buildingId": 10,
                                    "performerId": 5,
                                    "startTime": "2025-08-28T14:00:00Z",
                                    "endTime": "2025-08-30T14:00:00Z",
                                    "reservationStatus": "REGISTERED"
                                   }
                            """)
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "유효하지 않은 날짜 범위",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name="동일일자/역순", value = """
                    { "code":"INVALID_RANGE","message":"invalid range","status":400,
                      "path":"/api/v1/reservations","timestamp":"2025-08-14T10:00:00Z" }"""),
                                    @ExampleObject(name="6개월 초과", value = """
                    { "code":"INVALID_RANGE","message":"최대 6개월 범위만 지정할 수 있습니다.","status":400,
                      "path":"/api/v1/reservations","timestamp":"2025-08-14T10:00:00Z" }""")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "빌딩/수행자 미존재",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name="BUILDING_NOT_FOUND", value = """
                    { "code":"BUILDING_NOT_FOUND","message":"building not found","status":404,
                      "path":"/api/v1/reservations","timestamp":"2025-08-14T10:00:00Z" }"""),
                                    @ExampleObject(name="PERFORMER_NOT_FOUND", value = """
                    { "code":"PERFORMER_NOT_FOUND","message":"performer not found","status":404,
                      "path":"/api/v1/reservations","timestamp":"2025-08-14T10:00:00Z" }""")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "이미 점유된 기간",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name="RESERVATION_ALREADY_OCCUPIED", value = """
                { "code":"RESERVATION_ALREADY_OCCUPIED","message":"해당 기간에 이미 예약이 있습니다.","status":409,
                  "path":"/api/v1/reservations","timestamp":"2025-08-14T10:00:00Z" }""")
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "역할 권한 없음(PERFORMER 아님)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name="FORBIDDEN", value = """
                { "code":"FORBIDDEN","message":"권한이 없습니다.","status":403,
                  "path":"/api/v1/reservations","timestamp":"2025-08-14T10:00:00Z" }""")
                    )
            )
    })
    @PostMapping("/reservations")
    ReservationResDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateReservationReqDto.class),
                            examples = @ExampleObject(name="REQUEST", value = """
                {
                  "buildingId": 7,
                  "checkInDate": "2025-09-01",
                  "checkOutDate": "2025-09-10"
                }""")
                    )
            )
            @RequestBody CreateReservationReqDto req,
            @Parameter(description = "요청자 역할(PERFORMER)", required = true, example = "PERFORMER")
            @RequestHeader("X-Role") Role role
    );

    // 2) 예약 취소
    @Operation(summary = "예약 취소", description = "PERFORMER/OWNER 모두 취소 가능.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "취소 성공(본문 없음)"),
            @ApiResponse(
                    responseCode = "400", description = "체크인 이후 또는 완료 예약 취소 불가",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name="IN_USE", value = """
                    { "code":"RESERVATION_CANCEL_NOT_ALLOWED","message":"체크인 이후에는 취소할 수 없습니다.","status":400,
                      "path":"/api/v1/reservations/{id}/cancel","timestamp":"2025-08-14T10:00:00Z" }"""),
                                    @ExampleObject(name="COMPLETE", value = """
                    { "code":"RESERVATION_CANCEL_NOT_ALLOWED","message":"이미 이용이 완료된 예약입니다.","status":400,
                      "path":"/api/v1/reservations/{id}/cancel","timestamp":"2025-08-14T10:00:00Z" }""")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "예약 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name="RESERVATION_NOT_FOUND", value = """
                { "code":"RESERVATION_NOT_FOUND","message":"reservation not found","status":404,
                  "path":"/api/v1/reservations/{id}/cancel","timestamp":"2025-08-14T10:00:00Z" }""")
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "권한 없음(본인 아님 등)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name="FORBIDDEN", value = """
                { "code":"FORBIDDEN","message":"권한이 없습니다.","status":403,
                  "path":"/api/v1/reservations/{id}/cancel","timestamp":"2025-08-14T10:00:00Z" }""")
                    )
            )
    })
    @PostMapping("/reservations/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void cancel(
            @Parameter(description="예약 ID", example="10") @PathVariable Long id,
            @Parameter(description="요청자 역할(PERFORMER/OWNER)", required = true, example = "OWNER")
            @RequestHeader("X-Role") Role role
    );

    // 3) 점유일 조회
    @Operation(summary = "점유일 조회", description = "기간[from, to] 안의 예약으로 점유된 날짜 목록 반환")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OccupiedDayResDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "범위 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name="동일일자/역순", value = """
                    { "code":"INVALID_RANGE","message":"invalid range","status":400,
                      "path":"/api/v1/reservations/{buildingId}/occupied-days","timestamp":"2025-08-14T10:00:00Z" }"""),
                                    @ExampleObject(name="6개월 초과", value = """
                    { "code":"INVALID_RANGE","message":"최대 6개월 범위만 지정할 수 있습니다.","status":400,
                      "path":"/api/v1/reservations/{buildingId}/occupied-days","timestamp":"2025-08-14T10:00:00Z" }""")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "빌딩 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name="BUILDING_NOT_FOUND", value = """
                { "code":"BUILDING_NOT_FOUND","message":"building not found","status":404,
                  "path":"/api/v1/reservations/{buildingId}/occupied-days","timestamp":"2025-08-14T10:00:00Z" }""")
                    )
            )
    })
    @GetMapping("/reservations/{buildingId}/occupied-days")
    List<OccupiedDayResDto> occupiedDays(
            @Parameter(description="건물 ID", example="7") @PathVariable Long buildingId,
            @Parameter(description="조회 시작일", example="2025-09-01",
                    schema=@Schema(type="string", format="date"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description="조회 종료일", example="2025-09-30",
                    schema=@Schema(type="string", format="date"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    );
}
