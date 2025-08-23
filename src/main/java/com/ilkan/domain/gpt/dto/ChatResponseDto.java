package com.ilkan.domain.gpt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "ChatResponseDto", description = "S3 업로드 후 반환되는 이미지 URL 응답")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {

    @Schema(description = "S3 또는 CDN에 업로드된 최종 이미지 URL",
            example = "https://cdn.example.com/users/1/gpt/edit-1234abcd.png")
    private String url;
}
