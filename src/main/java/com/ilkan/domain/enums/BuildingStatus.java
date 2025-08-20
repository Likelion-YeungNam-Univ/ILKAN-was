package com.ilkan.domain.enums;

public enum BuildingStatus {
    REGISTERED, // 등록된 (건물주 공간조회)
    PAY_USEWAITING, // 결제 완료 및 사용대기
    APPLY_REVIEW, // 심사 신청
    IN_REVIEW, // 심사중
    END_REVIEW // 심사완료
}
