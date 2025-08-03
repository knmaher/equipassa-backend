package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.membership.SubscriptionService;
import com.equipassa.equipassa.membership.dto.MembershipRenewalResponse;
import com.equipassa.equipassa.membership.dto.OrgSubscriptionResponse;
import com.equipassa.equipassa.membership.dto.SubscriptionRenewRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/organizations/{orgId}/renew")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public ResponseEntity<OrgSubscriptionResponse> renewOrg(
            @PathVariable final Long orgId,
            @RequestBody @Valid final SubscriptionRenewRequest subscriptionRenewRequest
    ) {
        return ResponseEntity.ok(subscriptionService.renewOrg(orgId, subscriptionRenewRequest.extensionMonths()));
    }

    @PostMapping("/users/{userId}/renew")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','STAFF') or #userId == principal.id")
    public ResponseEntity<MembershipRenewalResponse> renewUser(
            @PathVariable final Long userId,
            @RequestBody @Valid final SubscriptionRenewRequest subscriptionRenewRequest
    ) {
        return ResponseEntity.ok(subscriptionService.renewUser(userId, subscriptionRenewRequest.extensionMonths()));
    }
}
