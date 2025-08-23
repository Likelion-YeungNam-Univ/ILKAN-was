// src/main/java/com/ilkan/domain/gpt/service/GptImageUploadService.java
package com.ilkan.domain.gpt.service;

import com.ilkan.domain.gpt.dto.ChatRequestDto;
import com.ilkan.domain.gpt.dto.GptImageResultDto;
import com.ilkan.domain.file.service.FileStorageService;
import com.ilkan.exception.GptImageExceptions; // ← 커스텀 예외
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GptImageUploadService {

    private final ChatGPTService chatGPTService;
    private final FileStorageService fileStorage;

    /**
     * GPT 이미지 편집 후 결과를 S3에 업로드하고 접근 URL을 반환한다.
     *
     * @param userId 업로드 사용자 ID
     * @param req    이미지와 프롬프트 요청 DTO
     * @return 업로드된 이미지의 접근 URL
     * @throws GptImageExceptions.BadOpenAiResponse OpenAI가 잘못된 Base64 응답을 반환한 경우
     * @throws GptImageExceptions.InvalidBase64     Base64 디코딩 실패 시
     * @throws GptImageExceptions.UploadFailed      S3 업로드 실패 시
     */

    public String editAndUpload(Long userId, ChatRequestDto req) {
        // 1. GPT 호출
        GptImageResultDto res = chatGPTService.editImage(req);

        // 2. base64 검증/정리
        String b64 = res.getBase64Image();
        if (b64 == null || b64.isBlank()) {
            throw new GptImageExceptions.BadOpenAiResponse("OpenAI에서 비어 있는 Base64 문자열을 반환했습니다.");
        }
        int comma = b64.indexOf(',');
        if (comma > 0) b64 = b64.substring(comma + 1);

        // 3. 디코드
        final byte[] bytes;
        try {
            bytes = java.util.Base64.getDecoder().decode(b64);
        } catch (IllegalArgumentException iae) {
            throw new GptImageExceptions.InvalidBase64("잘못된 Base64 형식입니다.", iae);
        }

        // 4. 업로드
        try {
            return fileStorage.uploadBytes(userId, bytes, "image/png", "edit");
        } catch (Exception e) {
            throw new GptImageExceptions.UploadFailed("S3 업로드에 실패했습니다.", e);
        }
    }
}
