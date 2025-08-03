package com.equipassa.equipassa.security.service;

import com.equipassa.equipassa.model.BackupCode;
import com.equipassa.equipassa.repository.BackupCodeRepository;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BackupCodeService {

    private final BackupCodeRepository backupCodeRepository;
    private static final int CODE_LENGTH = 8;
    private static final int NUMBER_OF_CODES = 5;

    public BackupCodeService(final BackupCodeRepository backupCodeRepository) {
        this.backupCodeRepository = backupCodeRepository;
    }

    public List<String> generateBackupCodes(final Long userId) {
        final List<String> plainCodes = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CODES; i++) {
            final String code = generateRandomCode(CODE_LENGTH);
            plainCodes.add(code);

            final BackupCode backupCode = new BackupCode();
            backupCode.setUserId(userId);
            backupCode.setCodeHash(hashCode(code));
            backupCodeRepository.save(backupCode);
        }
        return plainCodes;
    }

    public boolean verifyBackupCode(final Long userId, final String code) {
        final String hashed = hashCode(code);
        return backupCodeRepository.findByUserIdAndCodeHashAndUsedFalse(userId, hashed)
                .map(bc -> {
                    bc.setUsed(true);
                    backupCodeRepository.save(bc);
                    return true;
                })
                .orElse(false);
    }

    private String generateRandomCode(final int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final Random rnd = new Random();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String hashCode(final String code) {
        return Hashing.sha256()
                .hashString(code, StandardCharsets.UTF_8)
                .toString();
    }
}
