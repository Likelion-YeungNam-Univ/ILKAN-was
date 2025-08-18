package com.ilkan.controller.api;

import com.ilkan.domain.enums.Role;
import com.ilkan.dto.userdto.UserProfileResDto;
import com.ilkan.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "User", description = "프로필 조회 API")
@RequestMapping(value = "/api/v1", produces = "application/json")
public interface UserApi {

    @Operation(
            summary = "내 프로필 조회",
            description = "헤더의 역할에 해당하는 사용자 프로필을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfileResDto.class),
                            examples = @ExampleObject(name = "SUCCESS", value = """
                            {
                              "userId": 3,
                              "name": "아년석",
                              "phoneNumber": "010-1234-5678",
                              "role": "PERFORMER",
                              "profileImage": "https://cdn.example.com/u/3/profile.jpg",
                              "portfolioUrl": "https://pf.example.com/hong",
                              "organization": null,
                              "address": "대구광역시 북구 동천동 떡잎방범대 3호",
                              "education": "영남대학교 컴퓨터공학과 23학번",
                              "age": 22,
                              "gender": "여자"
                            }""")
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "헤더 누락/역할 문자열 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name="ROLE-MISSING", value = """
                                    {
                                      "code":"ROLE-MISSING",
                                      "message":"헤더 X-Role 이 필요합니다.",
                                      "status":401,
                                      "path":"/api/v1/myprofile",
                                      "timestamp":"2025-08-17T10:00:00Z"
                                    }"""),
                                    @ExampleObject(name="ROLE_INVALID", value = """
                                    {
                                      "code":"ROLE_INVALID",
                                      "message":"유효하지 않은 역할: PERFORME",
                                      "status":401,
                                      "path":"/api/v1/myprofile",
                                      "timestamp":"2025-08-17T10:00:00Z"
                                    }""")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 역할의 유저 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name="USER_NOT_FOUND", value = """
                            {
                              "code":"USER_NOT_FOUND",
                              "message":"역할에 해당하는 유저가 없습니다. =PERFORMER",
                              "status":404,
                              "path":"/api/v1/myprofile",
                              "timestamp":"2025-08-17T10:00:00Z"
                            }""")
                    )
            )
    })
    @GetMapping("/myprofile")
    UserProfileResDto getUserProfile(
            @Parameter(description = "요청자 역할", required = true, example = "PERFORMER")
            @RequestHeader("X-Role") Role role
    );
}
