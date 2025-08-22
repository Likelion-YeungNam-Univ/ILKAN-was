package com.ilkan.gemini.dto;

public class GeminiResponseDto {

    private String textResult;   // 텍스트 결과
    private String imageBase64;  // 이미지 base64 결과
    private String imageMime;    // "image/png", "image/webp" 등 (optional)

    public GeminiResponseDto() {}

    public GeminiResponseDto(String textResult, String imageBase64) {
        this.textResult = textResult;
        this.imageBase64 = imageBase64;
    }

    public GeminiResponseDto(String textResult, String imageBase64, String imageMime) {
        this.textResult = textResult;
        this.imageBase64 = imageBase64;
        this.imageMime = imageMime;
    }

    public String getTextResult() { return textResult; }
    public void setTextResult(String textResult) { this.textResult = textResult; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public String getImageMime() { return imageMime; }
    public void setImageMime(String imageMime) { this.imageMime = imageMime; }
}
