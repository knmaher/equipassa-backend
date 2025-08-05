package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.dto.UserResponse;
import com.equipassa.equipassa.security.CurrentUser;
import com.equipassa.equipassa.security.CustomUserDetails;
import com.equipassa.equipassa.security.dto.AcceptInviteRequest;
import com.equipassa.equipassa.security.dto.InviteUserRequest;
import com.equipassa.equipassa.security.service.InvitationService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(final InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/invite")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public ResponseEntity<String> invite(
            @RequestBody @Valid final InviteUserRequest request,
            @CurrentUser @Parameter(hidden = true) final CustomUserDetails admin
    ) {
        final String token = invitationService.sendInvitation(request, admin.getId()).getToken();
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/accept-invite")
    public ResponseEntity<UserResponse> acceptInvite(
            @RequestBody @Valid final AcceptInviteRequest request
    ) {
        final UserResponse response = invitationService.acceptInvitation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
