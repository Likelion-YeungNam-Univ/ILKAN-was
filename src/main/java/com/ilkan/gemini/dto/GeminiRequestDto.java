package com.ilkan.gemini.dto;

public class GeminiRequestDto {

    private String prompt;          // 프롬프트
    private String imageBase64;     // base64 인코딩된 이미지 (optional)
    private String mimeType;        // 이미지 타입 ("image/jpeg", "image/png" 등, optional)

    public GeminiRequestDto() {}

    public GeminiRequestDto(String prompt, String imageBase64, String mimeType) {
        this.prompt = prompt;
        this.imageBase64 = imageBase64;
        this.mimeType = mimeType;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
