package com.ilkan.security;

import com.ilkan.domain.profile.entity.enums.Role;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AllowedRoles {
    Role[] value();
}
