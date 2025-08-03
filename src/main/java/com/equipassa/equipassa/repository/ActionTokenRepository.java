package com.equipassa.equipassa.repository;

import com.equipassa.equipassa.model.ActionToken;
import com.equipassa.equipassa.model.ActionTokenType;
import com.equipassa.equipassa.model.TokenVisibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActionTokenRepository extends JpaRepository<ActionToken, Long> {
    Optional<ActionToken> findByTokenAndTypeAndVisibilityAndConsumedFalse(
            String token, ActionTokenType type, TokenVisibility visibility
    );
}
