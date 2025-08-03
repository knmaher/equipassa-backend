package com.equipassa.equipassa.service.token;

import com.equipassa.equipassa.model.ActionToken;
import com.equipassa.equipassa.model.ActionTokenType;
import com.equipassa.equipassa.model.TokenVisibility;
import com.equipassa.equipassa.repository.ActionTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationTokenProvider implements ActionTokenProvider {
    private final ActionTokenRepository actionTokenRepository;

    public EmailVerificationTokenProvider(final ActionTokenRepository actionTokenRepository) {
        this.actionTokenRepository = actionTokenRepository;
    }

    @Override
    public ActionToken generateToken(final Long userId, final int expirationMinutes) {
        final ActionToken token = new ActionToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUserId(userId);
        token.setType(ActionTokenType.EMAIL_VERIFICATION);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(expirationMinutes));
        token.setConsumed(false);
        token.setVisibility(TokenVisibility.PUBLIC);
        return actionTokenRepository.save(token);
    }

    @Override
    public ActionToken validateToken(final String token) {
        final Optional<ActionToken> actionToken = actionTokenRepository.findByTokenAndTypeAndVisibilityAndConsumedFalse(
                token, ActionTokenType.EMAIL_VERIFICATION, TokenVisibility.PUBLIC
        );

        if (actionToken.isPresent()) {
            final ActionToken tokenStr = actionToken.get();
            if (LocalDateTime.now().isBefore(tokenStr.getExpiresAt())) {
                return tokenStr;
            }
        }
        return null;
    }

    @Override
    public void consumeToken(final String token) {
        final Optional<ActionToken> actionToken = actionTokenRepository.findByTokenAndTypeAndVisibilityAndConsumedFalse(
                token, ActionTokenType.EMAIL_VERIFICATION, TokenVisibility.PUBLIC
        );

        actionToken.ifPresent(tokenStr -> {
            tokenStr.setConsumed(true);
            actionTokenRepository.save(tokenStr);
        });
    }

    @Override
    public ActionTokenType getSupportedTokenType() {
        return ActionTokenType.EMAIL_VERIFICATION;
    }
}
