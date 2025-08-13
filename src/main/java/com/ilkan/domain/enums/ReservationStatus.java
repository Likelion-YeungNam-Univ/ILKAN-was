package com.ilkan.domain.enums;

// 건물 상태
public enum ReservationStatus {
    REGISTERED, // 등록된 (건물주 공간조회)
    RESERVED, // 예약 중
    IN_USE, // 사용 중
    COMPLETE, // 사용 완료
    CANCELED // 취소
}
