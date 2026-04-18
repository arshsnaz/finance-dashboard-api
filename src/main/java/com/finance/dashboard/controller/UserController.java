package com.finance.dashboard.controller;

import com.finance.dashboard.dto.response.ApiResponse;
import com.finance.dashboard.enums.Role;
import com.finance.dashboard.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse(200, "Users fetched", userService.getAllUsers()));
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateUserRole(@PathVariable Long id,
                                                      @RequestParam Role role) {
        return ResponseEntity.ok(new ApiResponse(200, "Role updated", userService.updateUserRole(id, role)));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateUserStatus(@PathVariable Long id,
                                                        @RequestParam Boolean isActive) {
        return ResponseEntity.ok(new ApiResponse(200, "Status updated", userService.updateUserStatus(id, isActive)));
    }
}
