    package com.ilkan.domain.profile.service;

    import com.ilkan.domain.profile.entity.User;
    import com.ilkan.domain.profile.entity.enums.Role;
    import com.ilkan.domain.profile.dto.UserProfileResDto;
    import com.ilkan.exception.RoleExceptions;
    import com.ilkan.domain.auth.repository.UserRepository;
    import com.ilkan.util.RoleMapper;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    @Service
    @RequiredArgsConstructor
    public class UserService {
        private final UserRepository userRepository;

        /**
         * 주어진 {@link Role} 에 해당하는 사용자의 프로필을 조회합니다.
         *
         * @param role 프로필 조회 요청자 역할
         * @return 사용자 프로필 응답 DTO
         * @throws com.ilkan.exception.RoleExceptions.NotFound
         *        해당 역할의 사용자가 존재하지 않는 경우
         */

        @Transactional(readOnly = true)
        public UserProfileResDto getUserProfile(Role role) {
            Long userId = RoleMapper.getUserIdByRole(role.name()); // Role → userId 매핑
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RoleExceptions.NotFound(role));


            return UserProfileResDto.fromEntity(user);
        }

    }
