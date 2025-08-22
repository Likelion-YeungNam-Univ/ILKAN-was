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

    public UploadResult upload(Long userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("empty file");
        if (file.getSize() > 20L * 1024 * 1024) throw new IllegalArgumentException("file too large");
        String ct = file.getContentType() == null ? "application/octet-stream" : file.getContentType();
        if (!ct.startsWith("image/")) throw new IllegalArgumentException("only image/* allowed");

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

    public String getFileUrl(String key) {
        return cdnBaseUrl + "/" + key;
    }

}
