CREATE TABLE IF NOT EXISTS addresses
(
    id          BIGSERIAL PRIMARY KEY,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by  VARCHAR(50)                         NOT NULL,
    updated_by  VARCHAR(50)                         NOT NULL,
    version     BIGINT                              NOT NULL DEFAULT 0,
    street      VARCHAR(255)                        NOT NULL,
    city        VARCHAR(100)                        NOT NULL,
    state       VARCHAR(100)                        NOT NULL,
    postal_code VARCHAR(20)                         NOT NULL,
    country     VARCHAR(100)                        NOT NULL
);

CREATE TABLE IF NOT EXISTS organizations
(
    id                BIGSERIAL PRIMARY KEY,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by        VARCHAR(50)                         NOT NULL,
    updated_by        VARCHAR(50)                         NOT NULL,
    version           BIGINT                              NOT NULL DEFAULT 0,
    name              VARCHAR(100)                        NOT NULL UNIQUE,
    subscription_tier VARCHAR(50),
    address_id        BIGINT,
    CONSTRAINT fk_organizations_address FOREIGN KEY (address_id)
        REFERENCES addresses (id)
        ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_organizations_address_id ON organizations (address_id);

CREATE TABLE IF NOT EXISTS users
(
    id                         BIGSERIAL PRIMARY KEY,
    created_at                 TIMESTAMP             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                 TIMESTAMP             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by                 VARCHAR(50)  NOT NULL,
    updated_by                 VARCHAR(50)  NOT NULL,
    version                    BIGINT       NOT NULL DEFAULT 0,
    first_name                 VARCHAR(100) NOT NULL,
    last_name                  VARCHAR(100) NOT NULL,
    email                      VARCHAR(254) NOT NULL UNIQUE,
    password                   VARCHAR(97)  NOT NULL,
    user_role                  VARCHAR(50)  NOT NULL,
    membership_status          VARCHAR(50)  NOT NULL,
    membership_expiration_date DATE,
    phone_number               VARCHAR(50),
    mfa_enabled                BOOLEAN      NOT NULL DEFAULT false,
    mfa_secret                 VARCHAR(32),
    organization_id            BIGINT       REFERENCES organizations (id) ON DELETE SET NULL,
    address_id                 BIGINT       REFERENCES addresses (id) ON DELETE SET NULL,
    email_verified             BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS tools
(
    id                 BIGSERIAL PRIMARY KEY,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by         VARCHAR(50)                         NOT NULL,
    updated_by         VARCHAR(50)                         NOT NULL,
    version            BIGINT                              NOT NULL DEFAULT 0,
    name               VARCHAR(100)                        NOT NULL,
    description        TEXT,
    category           VARCHAR(100)                        NOT NULL,
    condition_status   VARCHAR(50)                         NOT NULL,
    quantity_available INT                                 NOT NULL CHECK (quantity_available >= 0),
    image_url          VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS reservations
(
    id             BIGSERIAL PRIMARY KEY,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by     VARCHAR(50)                         NOT NULL,
    updated_by     VARCHAR(50)                         NOT NULL,
    version        BIGINT                              NOT NULL DEFAULT 0,
    user_id        BIGINT                              NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    tool_id        BIGINT                              NOT NULL REFERENCES tools (id) ON DELETE CASCADE,
    status         VARCHAR(50)                         NOT NULL,
    reserved_from  TIMESTAMP                           NOT NULL,
    reserved_until TIMESTAMP                           NOT NULL,
    checked_out_at TIMESTAMP,
    returned_at    TIMESTAMP,
    quantity       INT       DEFAULT 1 CHECK (quantity > 0),
    late_fee       DECIMAL(10, 2)
);

CREATE
    EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE token_blacklist
(
    id             UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    token_hash     VARCHAR(512) NOT NULL UNIQUE,
    blacklisted_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at     TIMESTAMP    NOT NULL
);

CREATE INDEX idx_token_blacklist_expires ON token_blacklist (expires_at);

CREATE TABLE security_audit_log
(
    id          BIGSERIAL PRIMARY KEY,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by  VARCHAR(50)                         NOT NULL,
    updated_by  VARCHAR(50)                         NOT NULL,
    version     BIGINT                              NOT NULL DEFAULT 0,
    user_id     BIGINT                              REFERENCES users (id) ON DELETE SET NULL,
    event_type  VARCHAR(50)                         NOT NULL,
    description TEXT                                NOT NULL,
    ip_address  VARCHAR(45)
);

CREATE INDEX idx_audit_event_type ON security_audit_log (event_type);

CREATE TABLE backup_codes
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(50)  NOT NULL,
    updated_by VARCHAR(50)  NOT NULL,
    version    BIGINT       NOT NULL DEFAULT 0,
    user_id    BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    code_hash  VARCHAR(256) NOT NULL UNIQUE,
    used       BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_backup_codes_user_id ON backup_codes (user_id);

CREATE TABLE refresh_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(50)  NOT NULL,
    updated_by VARCHAR(50)  NOT NULL,
    version    BIGINT       NOT NULL DEFAULT 0,
    user_id    BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token      VARCHAR(512) NOT NULL UNIQUE,
    issued_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP    NOT NULL,
    revoked    BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);

CREATE TABLE IF NOT EXISTS action_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(50)  NOT NULL,
    updated_by VARCHAR(50)  NOT NULL,
    version    BIGINT       NOT NULL DEFAULT 0,
    token      VARCHAR(255) NOT NULL UNIQUE,
    type       VARCHAR(50)  NOT NULL,
    user_id    BIGINT       NOT NULL,
    expires_at TIMESTAMP    NOT NULL,
    consumed   BOOLEAN      NOT NULL DEFAULT FALSE,
    visibility VARCHAR(50)  NOT NULL,
    CONSTRAINT fk_action_tokens_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_action_tokens_token ON action_tokens (token);
