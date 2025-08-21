package com.ilkan.domain.auth.repository;

import com.ilkan.domain.profile.entity.User;
import com.ilkan.domain.profile.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByRole(Role role);
    Optional<User> findTopByRoleOrderByIdDesc(Role role);
}
