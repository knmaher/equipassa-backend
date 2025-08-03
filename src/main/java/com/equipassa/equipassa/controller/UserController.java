package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.dto.UserDTO;
import com.equipassa.equipassa.dto.UserProfileRequest;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.security.CurrentUser;
import com.equipassa.equipassa.security.CustomUserDetails;
import com.equipassa.equipassa.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getProfile(@CurrentUser final CustomUserDetails currentUser) {
        final User user = userService.getById(currentUser.getId());
        return ResponseEntity.ok(UserDTO.from(user));
    }

    @PostMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> updateProfile(
            @CurrentUser final CustomUserDetails currentUser,
            @RequestBody @Valid final UserProfileRequest userProfileRequest
    ) {
        final User user = userService.updateProfile(currentUser.getId(), userProfileRequest);

        return ResponseEntity.ok(UserDTO.from(user));
    }
}
