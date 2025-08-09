package com.ilkan.dto.userdto;

import com.ilkan.domain.entity.User;
import com.ilkan.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {

    private final Long id;          // 회원 고유 ID
    private final String name;      // 이름
    private final String phoneNumber; // 전화번호
    private final Role role;        // 역할 (REQUESTER, PERFORMER, OWNER)


    // DB에서 조회한 Entity를 API 응답용 DTO로 변환하기 위함
    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }
}
