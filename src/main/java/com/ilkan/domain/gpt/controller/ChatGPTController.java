// src/main/java/com/ilkan/domain/gpt/controller/ChatGPTController.java
package com.ilkan.domain.gpt.controller;

import com.ilkan.domain.gpt.api.ChatGPTImageApi;
import com.ilkan.domain.gpt.dto.ChatRequestDto;
import com.ilkan.domain.gpt.dto.ChatResponseDto;
import com.ilkan.domain.gpt.service.GptImageUploadService;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.exception.GptImageExceptions;
import com.ilkan.exception.RoleExceptions;
import com.ilkan.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/gpt")
@RequiredArgsConstructor
public class ChatGPTController implements ChatGPTImageApi {

    private final GptImageUploadService gptImageUploadService;

    @Override
    @PostMapping(
            value = "/edit-image-url",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ChatResponseDto editImageAndReturnUrl(
            @RequestHeader("X-Role") String headerRole,
            @RequestPart("image") MultipartFile image,
            @RequestPart("prompt") String prompt
    ) {

        if (image == null || image.isEmpty()) {
            throw new GptImageExceptions.InvalidRequest("이미지 파일은 필수입니다.");
        }
        if (prompt == null || prompt.isBlank()) {
            throw new GptImageExceptions.InvalidRequest("프롬프트는 필수입니다.");
        }
        if (image.getContentType() != null && !image.getContentType().toLowerCase().startsWith("image/")) {
            throw new GptImageExceptions.InvalidRequest("이미지 MIME 타입만 허용됩니다 (image/*).");
        }

        final Role role;
        try {
            role = Role.valueOf(headerRole.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RoleExceptions.Invalid("X-Role=" + headerRole);
        }
        final Long userId = RoleMapper.getUserIdByRole(role.name());

        ChatRequestDto dto = new ChatRequestDto();
        dto.updateImage(image);
        dto.updatePrompt(prompt);

        String url = gptImageUploadService.editAndUpload(userId, dto);

        return new ChatResponseDto(url);
    }
}
