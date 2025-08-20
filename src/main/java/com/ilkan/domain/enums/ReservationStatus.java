package com.ilkan.domain.enums;

// 건물 예약 상태
public enum ReservationStatus {
    RESERVED, // 예약 중
    PAY_USEWAITING, // 결제 완료 및 사용대기
    IN_USE, // 사용 중
    COMPLETE, // 사용 완료
    CANCELED // 취소
}
