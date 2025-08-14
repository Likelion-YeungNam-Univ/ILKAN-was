package com.ilkan.controller;

import com.ilkan.dto.workdto.WorkDetailResDto;
import com.ilkan.dto.workdto.WorkListResDto;
import com.ilkan.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/works")
@RequiredArgsConstructor
public class WorksController {

    private final WorkService workService;

    // 모든 일거리 조회
    @GetMapping
    public ResponseEntity<Page<WorkListResDto>> getWorkList(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<WorkListResDto> worksList = workService.getWorkList(pageable);
        if (worksList.isEmpty()) {
            return ResponseEntity.ok().body(Page.empty()); // 성공 및 데이터는 없음
        }
        return ResponseEntity.ok(worksList);
    }

    // 일거리 상세 조회
    @GetMapping("/{taskId}")
    public ResponseEntity<WorkDetailResDto> getWorkDetail(@PathVariable Long taskId) {
        WorkDetailResDto workDetail = workService.getWorkDetail(taskId);
        return ResponseEntity.ok(workDetail);
    }



}
