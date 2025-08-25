package com.ilkan.domain.gpt.service;


import com.ilkan.domain.gpt.dto.ChatRequestDto;
import com.ilkan.domain.gpt.dto.GptImageResultDto;

public interface ChatGPTService {
    GptImageResultDto editImage(ChatRequestDto requestDto);
}
