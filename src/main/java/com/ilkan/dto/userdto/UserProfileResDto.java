package com.ilkan.dto.userdto;

import com.ilkan.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResDto {
    @Schema(description = "사용자 아이디", example = "2")
    private Long userId;

    @Schema(description = "사용자 이름", example = "아년석")
    private String name;

    @Schema(description = "전화번호", example = "010-1234-1234")
    private String phoneNumber;

    @Schema(description = "사용자 역할", example = "REQUESTER")
    private String role;

    @Schema(description = "프로필 사진", example = "https://cdn.example.com/u/3/profile.jpg")
    private String profileImage;

    @Schema(description = "조직", example = "와플대학")
    private String organization;

    @Schema(description = "주소", example = "대구광역시 북구 동천동 떡잎방범대 3호")
    private String address;

    @Schema(description = "학력", example = "영남대학교 컴퓨터공학과 23학번")
    private String education;

    @Schema(description = "나이", example = "22")
    private Integer age;

    @Schema(description = "성별", example = "여자")
    private String gender;

    public static UserProfileResDto fromEntity(User user) {
        return UserProfileResDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .profileImage(user.getProfileImage())
                .organization(user.getOrganization())
                .address(user.getAddress())
                .education(user.getEducation())
                .age(user.getAge())
                .gender(user.getGender().getKor())
                .build();
    }

}
