package com.ilkan.repository;

import com.ilkan.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// 내가 올린 일거리 보관함
public interface UserRepository extends JpaRepository<User, Long> {
}
