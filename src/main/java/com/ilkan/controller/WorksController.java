package com.ilkan.controller;

import com.ilkan.auth.AllowedRoles;
import com.ilkan.controller.api.WorksApi;
import com.ilkan.domain.entity.Work;
import com.ilkan.domain.enums.Role;
import com.ilkan.dto.workdto.WorkDetailResDto;
import com.ilkan.dto.workdto.WorkListResDto;
import com.ilkan.dto.workdto.WorkReqDto;
import com.ilkan.dto.workdto.WorkResDto;
import com.ilkan.dto.workdto.WorkUserResDto;
import com.ilkan.service.WorkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/works")
@RequiredArgsConstructor
@Tag(name = "Works", description = "일거리 조회 API")
public class WorksController implements WorksApi {

    private final WorkService workService;

    // 모든 일거리 조회
    @GetMapping
    public ResponseEntity<Page<WorkListResDto>> getWorkList(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<WorkListResDto> worksList = workService.getWorkList(pageable);
        return ResponseEntity.ok(worksList);
    }

    // 일거리 상세 조회
    @GetMapping("/{taskId}")
    public ResponseEntity<WorkDetailResDto> getWorkDetail(@PathVariable Long taskId) {
        WorkDetailResDto workDetail = workService.getWorkDetail(taskId);
        return ResponseEntity.ok(workDetail);
    }

    // 의뢰자 일거리 등록
    @AllowedRoles(Role.REQUESTER)
    @PostMapping
    public ResponseEntity<WorkResDto> createWork(
            @RequestParam Long requesterId,
            @RequestBody WorkReqDto dto) {

        Work savedWork = workService.createWork(requesterId, dto);
        return ResponseEntity.ok(WorkResDto.fromEntity(savedWork));
    }


    // 의뢰자 일거리 수정
    @AllowedRoles(Role.REQUESTER)
    @PutMapping("/{taskId}")
    public ResponseEntity<WorkUserResDto> updateWork(
            @PathVariable Long taskId,
            @RequestParam Long requesterId,
            @RequestBody WorkReqDto dto) {

        Work updatedWork = workService.updateWork(taskId, requesterId, dto);
        return ResponseEntity.ok(WorkUserResDto.fromEntity(updatedWork));
    }

    // 의뢰자 일거리 삭제
    @AllowedRoles(Role.REQUESTER)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteWork(
            @PathVariable Long taskId,
            @RequestParam Long requesterId) {

        workService.deleteWork(taskId, requesterId);
        return ResponseEntity.noContent().build();
    }

}
