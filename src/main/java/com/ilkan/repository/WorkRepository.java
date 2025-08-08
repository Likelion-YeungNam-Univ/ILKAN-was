package com.ilkan.repository;

import com.ilkan.domain.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Long> {
    Page<Work> findByRequesterId(Long requesterId, Pageable pageable);
}