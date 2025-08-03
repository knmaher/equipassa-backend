ALTER TABLE action_tokens
    ADD COLUMN IF NOT EXISTS invite_email VARCHAR(254);
ALTER TABLE action_tokens
    ADD COLUMN IF NOT EXISTS invite_role VARCHAR(50);
