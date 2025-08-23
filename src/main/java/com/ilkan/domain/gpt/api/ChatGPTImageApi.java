// src/main/java/com/ilkan/domain/gpt/api/ChatGPTImageApi.java
package com.ilkan.domain.gpt.api;

import com.ilkan.domain.gpt.dto.ChatResponseDto;
import com.ilkan.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "GPT Image", description = "이미지 편집 → S3 업로드 → URL 반환 API")
@RequestMapping(value = "/api/v1/gpt", produces = "application/json")
public interface ChatGPTImageApi {

    @Operation(
            summary = "이미지 편집 후 URL 반환",
            description = """
                업로드한 원본 이미지와 프롬프트로 OpenAI 편집 이미지를 생성하고,
                S3에 저장한 뒤 최종 접근 URL을 반환합니다.

                - 요청 헤더: **X-Role** 필수 (예: OWNER)
                - 전송 형식: **multipart/form-data**
                  - image: file (PNG/JPG) (필수)
                  - prompt: string (필수)
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatResponseDto.class),
                            examples = @ExampleObject(
                                    name = "SUCCESS",
                                    value = """
                            {
                              "url": "https://cdn.example.com/users/1/gpt/edit-1234abcd.png"
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청(헤더/폼 필드 누락, MIME 불일치 등)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "REQUEST_INVALID_IMAGE",
                                            value = """
                                {
                                  "code": "REQUEST_INVALID",
                                  "message": "이미지 파일은 필수입니다.",
                                  "status": 400,
                                  "path": "/api/v1/gpt/edit-image-url",
                                  "timestamp": "2025-08-23T10:00:00"
                                }
                                """
                                    ),
                                    @ExampleObject(
                                            name = "REQUEST_INVALID_PROMPT",
                                            value = """
                                {
                                  "code": "REQUEST_INVALID",
                                  "message": "프롬프트는 필수입니다.",
                                  "status": 400,
                                  "path": "/api/v1/gpt/edit-image-url",
                                  "timestamp": "2025-08-23T10:00:00"
                                }
                                """
                                    ),
                                    @ExampleObject(
                                            name = "REQUEST_INVALID_MIME",
                                            value = """
                                {
                                  "code": "REQUEST_INVALID",
                                  "message": "이미지 MIME 타입만 허용됩니다 (image/*).",
                                  "status": 400,
                                  "path": "/api/v1/gpt/edit-image-url",
                                  "timestamp": "2025-08-23T10:00:00"
                                }
                                """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "X-Role 헤더 누락/유효하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "ROLE_MISSING",
                                            value = """
                                {
                                  "code": "ROLE_INVALID",
                                  "message": "X-Role 값이 비어있거나 누락되었습니다.",
                                  "status": 401,
                                  "path": "/api/v1/gpt/edit-image-url",
                                  "timestamp": "2025-08-23T10:00:00"
                                }
                                """
                                    ),
                                    @ExampleObject(
                                            name = "ROLE_INVALID",
                                            value = """
                                {
                                  "code": "ROLE_INVALID",
                                  "message": "허용되지 않은 역할입니다: PERFORMER",
                                  "status": 401,
                                  "path": "/api/v1/gpt/edit-image-url",
                                  "timestamp": "2025-08-23T10:00:00"
                                }
                                """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "413",
                    description = "파일 용량 초과",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "FILE_TOO_LARGE",
                                    value = """
                            {
                              "code": "FILE_TOO_LARGE",
                              "message": "업로드한 파일의 용량이 제한을 초과했습니다.",
                              "status": 413,
                              "path": "/api/v1/gpt/edit-image-url",
                              "timestamp": "2025-08-23T10:00:00"
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "OpenAI/S3 오류 또는 응답 포맷 문제",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "OPENAI_429",
                                            value = """
                                {
                                  "code": "OPENAI_429",
                                  "message": "OpenAI 오류 429: 요청 한도를 초과했습니다.",
                                  "status": 502,
                                  "path": "/api/v1/gpt/edit-image-url",
                                  "timestamp": "2025-08-23T10:00:00"
                                }
                                """
                                    ),
                                    @ExampleObject(
                                            name = "OPENAI_BAD_RESPONSE",
                                            value = """
                                {
                                  "code": "OPENAI_BAD_RESPONSE",
                                  "message": "OpenAI 응답이 올바르지 않습니다: {data:[]}",
                                  "status": 502,
                                  "path": "/api/v1/gpt/edit-image-url",
                                  "timestamp": "2025-08-23T10:00:00"
                                }
                                """
                                    ),
                                    @ExampleObject(
                                            name = "INVALID_BASE64",
                                            value = """
                                {
                                  "code": "INVALID_BASE64",
                                  "message": "잘못된 Base64 형식입니다.",
                                  "status": 502,
                                  "path": "/api/v1/gpt/edit-image-url",
                                  "timestamp": "2025-08-23T10:00:00"
                                }
                                """
                                    ),
                                    @ExampleObject(
                                            name = "S3_ERROR",
                                            value = """
                                {
                                  "code": "S3_ERROR",
                                  "message": "S3 오류 403: AccessDenied",
                                  "status": 502,
                                  "path": "/api/v1/gpt/edit-image-url",
                                  "timestamp": "2025-08-23T10:00:00"
                                }
                                """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "504",
                    description = "업스트림 타임아웃(OpenAI 호출 등)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "UPSTREAM_TIMEOUT",
                                    value = """
                            {
                              "code": "UPSTREAM_TIMEOUT",
                              "message": "업스트림 타임아웃: 응답 지연으로 실패했습니다.",
                              "status": 504,
                              "path": "/api/v1/gpt/edit-image-url",
                              "timestamp": "2025-08-23T10:00:00"
                            }
                            """
                            )
                    )
            )
    })
    @PostMapping(
            value = "/edit-image-url",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ChatResponseDto editImageAndReturnUrl(
            @Parameter(description = "역할 헤더(예: OWNER)", required = true, example = "OWNER")
            @RequestHeader("X-Role") String roleHeader,

            @Parameter(description = "입력 이미지 파일 (PNG/JPG)", required = true)
            @RequestPart("image") MultipartFile image,

            @Parameter(description = "편집 프롬프트", required = true, example = "밝고 따뜻한 북유럽 인테리어 느낌으로 바꿔줘")
            @RequestPart("prompt") String prompt
    );
}
