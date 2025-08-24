package com.ilkan.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final S3Client s3;
    @Value("${app.s3.bucket}") private String bucket;

    /**
     * 멀티파트 파일을 S3에 업로드하고 결과 정보를 반환한다.
     *
     * @param userId 업로드 사용자 ID
     * @param file   업로드할 멀티파트 파일
     * @return 업로드 결과(키/URL/콘텐츠 타입/크기)
     * @throws IOException 파일 스트림 처리 실패 시
     * @throws IllegalArgumentException 잘못된 입력(빈 파일/과대용량/비이미지)인 경우
     */

    public UploadResult upload(Long userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("업로드할 파일이 비어 있습니다.");
        if (file.getSize() > 20L * 1024 * 1024) throw new IllegalArgumentException("파일 크기는 최대 20MB까지 허용됩니다.");
        String ct = file.getContentType() == null ? "application/octet-stream" : file.getContentType();
        if (!ct.startsWith("image/")) throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");

        String safe = (file.getOriginalFilename()==null ? "file" :
                file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_"));
        String key = "users/%d/uploads/%s_%s".formatted(userId, UUID.randomUUID(), safe);

        try (InputStream in = file.getInputStream()) {
            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket).key(key).contentType(ct).cacheControl("public, max-age=300").build(),
                    RequestBody.fromInputStream(in, file.getSize()));
        }
        String url = "https://%s.s3.ap-northeast-2.amazonaws.com/%s".formatted(bucket, key);
        return new UploadResult(key, url, ct, file.getSize());
    }

    public record UploadResult(String key, String url, String contentType, long size) {}

    @Value("${app.cdn-base-url}")
    private String cdnBaseUrl;

    /**
     * 바이트 배열을 S3에 업로드하고 공개 URL을 반환한다.
     *
     * @param userId      업로드 사용자 ID
     * @param bytes       업로드할 바이트 배열
     * @param contentType MIME 타입(없거나 비이미지면 image/png로 대체)
     * @param baseName    파일명 베이스(없으면 gpt-edit)
     * @return 업로드된 파일의 접근 URL
     * @throws IllegalArgumentException 바이트 배열이 비어 있는 경우
     */

    public String uploadBytes(Long userId, byte[] bytes, String contentType, String baseName) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("업로드할 데이터가 비어 있습니다.");
        if (contentType == null || !contentType.startsWith("image/")) {
            contentType = "image/png"; // 기본 png로
        }

        String safeBase = (baseName == null || baseName.isBlank()) ? "gpt-edit" :
                baseName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String key = "users/%d/gpt/%s_%s.png".formatted(userId, java.util.UUID.randomUUID(), safeBase);

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .cacheControl("public, max-age=31536000") // 1년 캐시
                        .build(),
                RequestBody.fromBytes(bytes)
        );

        if (cdnBaseUrl != null && !cdnBaseUrl.isBlank()) {
            return cdnBaseUrl + "/" + key;
        }
        return "https://%s.s3.ap-northeast-2.amazonaws.com/%s".formatted(bucket, key);
    }

    /**
     * 저장된 객체 키로 접근 URL을 생성한다.
     *
     * @param key S3 객체 키
     * @return 접근 URL
     */
    public String getFileUrl(String key) {
        return cdnBaseUrl + "/" + key;
    }

}
