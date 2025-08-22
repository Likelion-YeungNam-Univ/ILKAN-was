package com.ilkan.gemini.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilkan.gemini.dto.GeminiApiResponseDto;
import com.ilkan.gemini.dto.GeminiRequestDto;
import com.ilkan.gemini.dto.GeminiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;

@Service
public class GeminiServiceImpl implements GeminiService {

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public GeminiServiceImpl(
            WebClient.Builder webClientBuilder,
            @Value("${gemini.api-url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-preview-image-generation:generateContent}") String apiUrl,
            @Value("${gemini.api-key:}") String apiKey
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY가 설정되지 않았습니다.");
        }

        // 네트워크 타임아웃
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(30));

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();

        this.webClient = webClientBuilder
                .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .baseUrl(apiUrl)
                // 모든 요청에 API 키를 쿼리파라미터로 자동 부착
                .filter((request, next) -> {
                    var uri = request.url();
                    var newUri = UriComponentsBuilder.fromUri(uri)
                            .replaceQueryParam("key", apiKey)
                            .build(true)
                            .toUri();
                    var mutated = ClientRequest.from(request)  // 기존 요청을 기반으로
                            .url(newUri)                       // 새 URL 적용
                            .build();
                    return next.exchange(mutated);
                })
                .build();
    }

    /**
     * Gemini API를 호출하여 텍스트/이미지 응답을 가져온다.
     *
     * @param requestDto 프롬프트 및 선택적 이미지(Base64)를 포함한 요청 DTO
     * @return GeminiResponseDto 텍스트 및 이미지 Base64 응답
     */

    @Override
    public GeminiResponseDto getAnswer(GeminiRequestDto requestDto) {
        var textPart = java.util.Map.of("text", requestDto.getPrompt());

        java.util.Map<String, Object> imagePart = null;
        if (requestDto.getImageBase64() != null && requestDto.getMimeType() != null) {
            imagePart = java.util.Map.of(
                    "inlineData", java.util.Map.of(
                            "mimeType", requestDto.getMimeType(),
                            "data", requestDto.getImageBase64()
                    )
            );
        }

        Object[] partsArray = (imagePart != null)
                ? new Object[]{textPart, imagePart}
                : new Object[]{textPart};

        var content = java.util.Map.of(
                "role", "user",
                "parts", partsArray
        );

        var requestBody = java.util.Map.of(
                "contents", new Object[]{content},
                "generationConfig", java.util.Map.of(
                        "responseModalities", new String[]{"TEXT", "IMAGE"}
                )
        );

        //호출 & 에러 분기
        String raw = webClient.post()
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        s -> s.is4xxClientError() || s.is5xxServerError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Gemini API error: "
                                        + resp.statusCode().value() + " - " + body))
                )
                .bodyToMono(String.class)
                .block();

        if (raw == null) {
            // 비정상 응답
            return new GeminiResponseDto("", null);
        }

        // 응답 파싱 (DTO 사용)
        try {
            GeminiApiResponseDto api = mapper.readValue(raw, GeminiApiResponseDto.class);
            if (api.getCandidates() == null || api.getCandidates().isEmpty()) {
                return new GeminiResponseDto("", null);
            }

            GeminiApiResponseDto.Candidate cand = api.getCandidates().get(0);
            if (cand.getContent() == null || cand.getContent().getParts() == null) {
                return new GeminiResponseDto("", null);
            }

            String textResult = "";
            String imageBase64 = null;

            List<GeminiApiResponseDto.Part> parts = cand.getContent().getParts();
            for (GeminiApiResponseDto.Part p : parts) {
                if (p.getText() != null && !p.getText().isBlank()) {
                    textResult = p.getText();
                } else if (p.getInlineData() != null && p.getInlineData().getData() != null) {
                    imageBase64 = p.getInlineData().getData();
                }
            }

            return new GeminiResponseDto(textResult, imageBase64);

        } catch (Exception e) {
            // 파싱 실패 시 안전 반환
            return new GeminiResponseDto("", null);
        }
    }
}
