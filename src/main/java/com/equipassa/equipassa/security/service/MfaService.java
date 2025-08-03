package com.equipassa.equipassa.security.service;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class MfaService {
    private final DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();

    public String generateSecretKey() {
        return secretGenerator.generate();
    }

    public boolean validateCode(final String secret, final String code) {
        return verifier.isValidCode(secret, code);
    }

    public String generateQrCodeUri(final String secret, final String email) throws QrGenerationException {
        final QrData data = new QrData.Builder()
                .label("Equipassa:" + email)
                .secret(secret)
                .issuer("Equipassa")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        final byte[] imageData = qrGenerator.generate(data);
        final String mimeType = qrGenerator.getImageMimeType();

        return getDataUriForImage(imageData, mimeType);
    }
}
