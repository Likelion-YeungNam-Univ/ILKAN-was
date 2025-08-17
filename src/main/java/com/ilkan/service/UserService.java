    package com.ilkan.service;

    import com.ilkan.domain.entity.User;
    import com.ilkan.domain.enums.Role;
    import com.ilkan.dto.userdto.UserProfileResDto;
    import com.ilkan.exception.RoleExceptions;
    import com.ilkan.repository.UserRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.Optional;

    @Service
    @RequiredArgsConstructor
    public class UserService {
        private final UserRepository userRepository;

        @Transactional(readOnly = true)
        public UserProfileResDto getUserProfile(Role role) {
            User user = userRepository.findFirstByRole(role)
                    .orElseThrow(() -> new RoleExceptions.NotFound(role));

            return UserProfileResDto.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole().name())
                    .profileImage(user.getProfileImage())
                    .portfolioUrl(user.getPortfolioUrl())
                    .organization(user.getOrganization())
                    .build();

        }

    }
