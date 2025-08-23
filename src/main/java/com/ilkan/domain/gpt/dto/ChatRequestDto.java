package com.ilkan.domain.gpt.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Schema(name = "ChatRequestDto", description = "이미지 편집 요청")
public class ChatRequestDto {
    @Schema(description = "입력 이미지 파일 (PNG/JPG)", type = "string", format = "binary", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile image;

    @Schema(description = "편집 프롬프트", example = "사진을 밝고 따뜻한 북유럽 인테리어 느낌으로 바꿔줘", requiredMode = Schema.RequiredMode.REQUIRED)
    private String prompt;

    public void updateImage(MultipartFile image) {
        this.image = image;
    }

    public void updatePrompt(String prompt) {
        this.prompt = prompt;
    }


}