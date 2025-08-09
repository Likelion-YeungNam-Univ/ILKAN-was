package com.ilkan.repository;

import com.ilkan.domain.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

// 내가 올린 일거리 보관함
public interface UserRepository extends JpaRepository<Work, Long> {
}
