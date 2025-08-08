package com.ilkan.domain.entity;

import com.ilkan.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 용 기본 생성자(보호)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;


    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "phone_number" , nullable = false)
    private String phoneNumber;


    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
}