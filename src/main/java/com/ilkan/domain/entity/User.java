package com.ilkan.domain.entity;

import com.ilkan.domain.enums.Gender;
import com.ilkan.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number" , nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(name = "portfolio_url", length = 255)
    private String portfolioUrl;

    @Column(name = "organization", length = 20)
    private String organization;

    @Column(name = "email", length = 255, nullable = false)
    private String eMail;

    @Column(name = "address" , nullable = false)
    private String address;

    @Column(name = "education", length = 255)
    private String education;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    // ==== 변경 메서드 ====
    public void updateName(String name) {
        this.name = name;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateRole(Role role) { this.role = role; }

    public void updateProfileImage(String profileImage) { this.profileImage = profileImage; }

    public void updatePortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public void updateOrganization(String organization) {
        this.organization = organization;
    }

    public void updateEmail(String eMail) {this.eMail = eMail;}

}
