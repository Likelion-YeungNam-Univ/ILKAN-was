package com.ilkan.domain.file.controller;

import com.ilkan.domain.file.service.FileStorageService;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.exception.RoleExceptions;
import com.ilkan.security.AllowedRoles;
import com.ilkan.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/uploads")
public class FileUploadController {
    private final FileStorageService storage;

    @AllowedRoles(Role.OWNER)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestHeader("X-Role") String headerRole,
                                    @RequestPart("file") MultipartFile file) throws IOException {
        Role role;
        try {
            role = Role.valueOf(headerRole.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RoleExceptions.Invalid(headerRole);
        }

        Long userId = RoleMapper.getUserIdByRole(role.name());

        return ResponseEntity.ok(storage.upload(userId, file));
    }
}
