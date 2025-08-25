// src/main/java/com/ilkan/exception/GptImageExceptions.java
package com.ilkan.exception;

public final class GptImageExceptions {

    /** 요청 자체가 잘못된 경우 (이미지 없음, prompt 비어있음, MIME 불가 등) */
    public static class InvalidRequest extends RuntimeException {
        public InvalidRequest(String msg) { super(msg); }
    }

    /** OpenAI 응답 구조가 예상과 다를 때 (data/b64_json 누락 등) */
    public static class BadOpenAiResponse extends RuntimeException {
        public BadOpenAiResponse(String msg) { super(msg); }
    }

    /** base64 디코딩 실패 */
    public static class InvalidBase64 extends RuntimeException {
        public InvalidBase64(String msg, Throwable cause) { super(msg, cause); }
    }

    /** 업로드 실패(S3 등) - SDK 자체 예외(S3Exception)는 별도 핸들러가 받음 */
    public static class UploadFailed extends RuntimeException {
        public UploadFailed(String msg, Throwable cause) { super(msg, cause); }
    }
}
