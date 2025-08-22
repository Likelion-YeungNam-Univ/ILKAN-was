package com.ilkan.gemini.service;



import com.ilkan.gemini.dto.GeminiRequestDto;
import com.ilkan.gemini.dto.GeminiResponseDto;
import org.springframework.stereotype.Service;

public interface GeminiService {
    GeminiResponseDto getAnswer(GeminiRequestDto requestDto);
}
