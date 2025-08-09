package com.ilkan.dto.userdto;

import com.ilkan.domain.entity.User;
import com.ilkan.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserRequestDto {

    private final Long id;          // 회원 고유 ID
    private final String name;      // 이름
    private final String phoneNumber; // 전화번호
    private final Role role;        // 역할 (REQUESTER, PERFORMER, OWNER)

    // DB에 저장할 엔티티 객체 생성하기 위함
    public User toEntity() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .role(this.role)
                .build();
    }
}
