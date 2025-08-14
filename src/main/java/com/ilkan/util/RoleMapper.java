package com.ilkan.util;

public final class RoleMapper { // 상속방지 및 역할고정

    private RoleMapper() {} //인스턴스 생성방지

    // role → userId 매핑
    public static Long getUserIdByRole(String role) {
        if(role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        switch (role.toUpperCase()) {
            case "REQUESTER": return 1L;
            case "PERFORMER": return 2L;
            case "OWNER": return 3L;
            default: throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
