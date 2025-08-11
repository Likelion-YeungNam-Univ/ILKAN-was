package com.ilkan.auth;

import com.ilkan.domain.enums.Role;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AllowedRoles {
    Role[] value();
}
