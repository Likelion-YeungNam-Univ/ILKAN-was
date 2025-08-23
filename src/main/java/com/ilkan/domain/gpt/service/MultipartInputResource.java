package com.ilkan.domain.gpt.service;


import org.springframework.core.io.ByteArrayResource;

public class MultipartInputResource extends ByteArrayResource {

    private final String filename;

    /**
     * 바이트 배열과 파일명을 가진 리소스를 생성한다.
     *
     * @param byteArray 파일 바이트 배열
     * @param filename  원본 파일명
     */

    public MultipartInputResource(byte[] byteArray, String filename) {
        super(byteArray);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }
}
