package com.equipassa.equipassa.service.token;

import com.equipassa.equipassa.model.ActionToken;
import com.equipassa.equipassa.model.ActionTokenType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActionTokenContext {

    private final Map<ActionTokenType, ActionTokenProvider> tokenProviderMap;

    public ActionTokenContext(final List<ActionTokenProvider> actionTokenProviders) {
        tokenProviderMap = actionTokenProviders.stream()
                .collect(Collectors.toMap(ActionTokenProvider::getSupportedTokenType, s -> s));
    }

    public ActionToken generateToken(final ActionTokenType type, final Long userId, final int expirationMinutes) {
        final ActionTokenProvider actionTokenProvider = tokenProviderMap.get(type);
        if (actionTokenProvider == null) {
            throw new IllegalArgumentException("No Token Provider found for type: " + type);
        }
        return actionTokenProvider.generateToken(userId, expirationMinutes);
    }

    public ActionToken validateToken(final ActionTokenType type, final String token) {
        final ActionTokenProvider actionTokenProvider = tokenProviderMap.get(type);
        if (actionTokenProvider == null) {
            throw new IllegalArgumentException("No Token Provider found for type: " + type);
        }
        return actionTokenProvider.validateToken(token);
    }

    public void consumeToken(final ActionTokenType type, final String token) {
        final ActionTokenProvider actionTokenProvider = tokenProviderMap.get(type);
        if (actionTokenProvider == null) {
            throw new IllegalArgumentException("No Token Provider found for type: " + type);
        }
        actionTokenProvider.consumeToken(token);
    }
}
