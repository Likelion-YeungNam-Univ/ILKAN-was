// src/main/java/com/ilkan/domain/gpt/service/GptImageServiceImpl.java
package com.ilkan.domain.gpt.service;

import com.ilkan.domain.gpt.dto.ChatRequestDto;
import com.ilkan.domain.gpt.dto.GptImageResultDto;
import com.ilkan.exception.GptImageExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptImageServiceImpl implements ChatGPTService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/images/edits";

    /**
     * 이미지와 프롬프트를 OpenAI에 전달하여 편집된 이미지(Base64)를 반환한다.
     *
     * @param requestDto 이미지 파일과 프롬프트 요청 DTO
     * @return 편집된 이미지(Base64) DTO
     * @throws GptImageExceptions.InvalidRequest     이미지 바이트 읽기 실패 시
     * @throws GptImageExceptions.BadOpenAiResponse  OpenAI 응답이 잘못되었을 때
     */
    @Override
    public GptImageResultDto editImage(ChatRequestDto requestDto) {
        RestTemplate restTemplate = new RestTemplate();

        MultipartFile imageFile = requestDto.getImage();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(apiKey);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            body.add("image", new MultipartInputResource(imageFile.getBytes(), imageFile.getOriginalFilename()));
        } catch (Exception e) {
            throw new GptImageExceptions.InvalidRequest("이미지 바이트를 읽는 데 실패했습니다.");
        }

        body.add("prompt", requestDto.getPrompt());
        body.add("model", "gpt-image-1");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("data")) {
            throw new GptImageExceptions.BadOpenAiResponse("OpenAI 응답이 올바르지 않습니다: " + responseBody);
        }

        Map firstData = ((java.util.List<Map>) responseBody.get("data")).get(0);
        String base64Image = (String) firstData.get("b64_json");
        if (base64Image == null) {
            throw new GptImageExceptions.BadOpenAiResponse("응답에 b64_json 값이 없습니다.");
        }

        return new GptImageResultDto(base64Image);
    }
}
