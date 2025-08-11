package com.ilkan.controller;

import com.ilkan.dto.workdto.WorkResponseDto;
import com.ilkan.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/my/commissions")
@RequiredArgsConstructor
public class UserWorkController {
    private final WorkService workService;

    // 내가 등록한 일거리 조회 (의뢰자)
    @GetMapping("/upload")
    public ResponseEntity<Page<WorkResponseDto>> getMyUploadedWorks(
            @RequestParam Long userId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // 서비스 호출하여 DTO로 변환된 페이징 결과 조회
        Page<WorkResponseDto> worksDto = workService.getWorksByRequester(userId, pageable);
        return ResponseEntity.ok(worksDto);
    }

    // 내가 수행중인 일거리 조회 (수행자)
    @GetMapping("/doing")
    public ResponseEntity<Page<WorkResponseDto>> doingWorksByPerformer(
            @RequestParam Long userId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<WorkResponseDto> worksDto = workService.doingWorksByPerformer(userId, pageable);
        return ResponseEntity.ok(worksDto);
    }
}
