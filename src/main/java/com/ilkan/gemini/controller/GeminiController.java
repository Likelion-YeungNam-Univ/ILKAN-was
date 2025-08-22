package com.ilkan.gemini.controller;


import com.ilkan.gemini.dto.GeminiRequestDto;
import com.ilkan.gemini.dto.GeminiResponseDto;
import com.ilkan.gemini.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/ask")
    public ResponseEntity<GeminiResponseDto> askQuestion(@RequestBody GeminiRequestDto requestDto) {
        GeminiResponseDto responseDto = geminiService.getAnswer(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}