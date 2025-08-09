package com.ilkan.service;

import com.ilkan.domain.entity.Work;
import com.ilkan.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;

    // createdAt 기준 내림차순 등록한 작업 조회
    public Page<Work> getWorksByRequester(Long requesterId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending()); // 페이징처리
        return workRepository.findByRequesterId(requesterId, pageable);
    }
}
