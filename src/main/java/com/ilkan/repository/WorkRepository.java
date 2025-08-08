package com.ilkan.repository;

import com.ilkan.domain.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work,Long> {
}
