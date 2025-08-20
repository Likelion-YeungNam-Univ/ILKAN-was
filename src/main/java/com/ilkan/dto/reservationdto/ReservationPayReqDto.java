package com.ilkan.dto.reservationdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "시연용 카드정보 request DTO / DB 저장 X")
public class ReservationPayReqDto {
    @Schema(description = "예약 id", example = "1")
    private Long reservationId;

    @Schema(description = "은행명",example = "국민은행")
    private String bankName;

    @Schema(description = "카드명의자" , example = "김이박")
    private String cardOwner;

    @Schema(description = "카드번호", example = "123-4213")
    private String cardNumber;

    @Schema(description = "cvc", example = "854")
    private String cvc;

    @Schema(description = "expiry", example = "MM/YY")
    private String expiry;

}
