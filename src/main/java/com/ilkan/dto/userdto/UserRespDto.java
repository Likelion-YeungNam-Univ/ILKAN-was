package com.ilkan.dto.userdto;

import com.ilkan.domain.entity.User;
import com.ilkan.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class UserRespDto {

    @Schema(description = "유저 ID", example = "1")
    private final Long id;          // 회원 고유 ID

    @Schema(description = "이름", example = "김이박")
    private final String name;      // 이름

    @Schema(description = "전화번호", example = "010-1234-1234")
    private final String phoneNumber; // 전화번호

    @Schema(description = "유저 ID", example = "REQUESTER")
    private final Role role;        // 역할 (REQUESTER, PERFORMER, OWNER)


    // DB에서 조회한 Entity를 API 응답용 DTO로 변환하기 위함
    public static UserRespDto fromEntity(User user) {
        return UserRespDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }
}
