package com.ilkan.domain.entity;

import com.ilkan.domain.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 자동증가
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number" , nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column
    private String profileImage;

    @Column
    private String bio;

    @Column
    private String portfolioUrl;

    @Column
    private String organization;

    @Column
    private String eMail;



    // ==== 변경 메서드 ====
    public void updateName(String name) {
        this.name = name;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateRole(Role role) { this.role = role; }

    public void updateProfileImage(String profileImage) { this.profileImage = profileImage; }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    public void updatePortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public void updateOrganization(String organization) {
        this.organization = organization;
    }

    public void updateEmail(String eMail) {this.eMail = eMail;}

}
