package com.ilkan.domain.profile.entity.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남자"),
    FEMALE("여자");

    private final String kor;

    Gender(String kor) {
        this.kor = kor;
    }
}
