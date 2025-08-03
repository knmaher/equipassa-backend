package com.equipassa.equipassa.service.token;

import com.equipassa.equipassa.model.ActionToken;
import com.equipassa.equipassa.model.ActionTokenType;
import com.equipassa.equipassa.model.TokenVisibility;
import com.equipassa.equipassa.repository.ActionTokenRepository;
import com.equipassa.equipassa.util.EntityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenProviderTest {

    @Mock
    private ActionTokenRepository actionTokenRepository;

    @InjectMocks
    private PasswordResetTokenProvider provider;

    private ActionToken token;

    @BeforeEach
    void setUp() {
        token = EntityUtil.createActionToken(
                "token123",
                ActionTokenType.PASSWORD_RESET,
                1L,
                LocalDateTime.now().plusMinutes(10),
                false,
                TokenVisibility.PRIVATE
        );
    }

    @Test
    void consumeToken_marksTokenAsConsumed() {
        when(actionTokenRepository.findByTokenAndTypeAndVisibilityAndConsumedFalse(
                token.getToken(), ActionTokenType.PASSWORD_RESET, TokenVisibility.PRIVATE
        )).thenReturn(Optional.of(token));

        provider.consumeToken(token.getToken());

        assertThat(token.isConsumed()).isTrue();
        verify(actionTokenRepository).save(token);
    }
}