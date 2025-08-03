package com.equipassa.equipassa.service.token;

import com.equipassa.equipassa.model.ActionToken;
import com.equipassa.equipassa.model.ActionTokenType;

public interface ActionTokenProvider {

    ActionToken generateToken(final Long userId, final int expirationMinutes);

    ActionToken validateToken(final String token);

    void consumeToken(final String token);

    ActionTokenType getSupportedTokenType();
}
