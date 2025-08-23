// src/main/java/com/ilkan/domain/gpt/dto/GptImageResultDto.java
package com.ilkan.domain.gpt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(name = "GptImageResultDto", description = "OpenAI 이미지 API의 base64 결과")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptImageResultDto {
    @Schema(description = "base64로 인코딩된 PNG 이미지", example = "iVBORw0KGgoAAA...")
    private String base64Image;
}
