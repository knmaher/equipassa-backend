package com.equipassa.equipassa.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/kube")
public class KubeProbeController {

    private final String probeToken;

    public KubeProbeController(@Value("${kube.probe.token:}") final String probeToken) {
        this.probeToken = probeToken;
    }

    private static boolean constantTimeEquals(final String a, final String b) {
        if (a == null || b == null) return false;
        return MessageDigest.isEqual(
                a.getBytes(StandardCharsets.UTF_8),
                b.getBytes(StandardCharsets.UTF_8)
        );
    }

    private boolean validToken(final HttpServletRequest req) {
        return constantTimeEquals(req.getHeader("X-Probe-Token"), probeToken);
    }

    @GetMapping("/readiness")
    public ResponseEntity<Void> readiness(final HttpServletRequest req) {
        if (!validToken(req)) return ResponseEntity.status(401).build();
        // if you want: check DB/message-broker here and return 503 if not ready
        return ResponseEntity.ok().build();
    }

    @GetMapping("/liveness")
    public ResponseEntity<Void> liveness(final HttpServletRequest req) {
        if (!validToken(req)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok().build();
    }
}
