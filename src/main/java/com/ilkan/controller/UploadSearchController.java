package com.ilkan.controller;

import com.ilkan.domain.entity.Work;
import com.ilkan.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/my/commissions")
@RequiredArgsConstructor
public class UploadSearchController {
    private final WorkService workService;

    @GetMapping("/upload")
    public ResponseEntity<Page<Work>> getMyUploadedWorks(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 서비스 호출하여 페이징된 결과 조회
        Page<Work> works = workService.getWorksByRequester(userId, page, size);

        // 결과를 HTTP 200 OK와 함께 반환
        return ResponseEntity.ok(works);
    }
}
