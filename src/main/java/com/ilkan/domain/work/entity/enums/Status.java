package com.ilkan.domain.work.entity.enums;

// 일거리 상태
public enum Status {
    OPEN, // 모집중
    APPLY_TO, // 지원함
    ASSIGNED, // 배정됨
    IN_PROGRESS, // 진행중
    PAY_WAITING, // 보수 지급대기 (수행자가 수행완료 눌렀을떄)
    COMPLETE_WAITING, // 완료버튼 대기상태 (의뢰자가 먼저 보수지급 눌렀을때)
    COMPLETED, // 완료됨
    CANCELLED; // 취소됨

    // 진행중(마이페이지에 표시할 그룹) 상태 집합 반환
    // 언제든 이 메서드 하나만 바꾸면 모든 조회가 같이 바뀜
    public static java.util.Set<Status> activeStatuses() {
        return java.util.EnumSet.of(IN_PROGRESS, PAY_WAITING, COMPLETE_WAITING);
    }
}
