package com.ilkan.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResDto {
    private Long userId;
    private String name;
    private String phoneNumber;
    private String role;
    private String profileImage;
    private String portfolioUrl;
    private String organization;
}
