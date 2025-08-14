package com.ilkan.repository;

import com.ilkan.domain.entity.User;
import com.ilkan.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByRole(Role role);
}
