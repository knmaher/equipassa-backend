DO $$
BEGIN
    IF to_regclass('public.refresh_tokens') IS NOT NULL THEN
        EXECUTE 'CREATE TABLE IF NOT EXISTS z_backup_refresh_tokens AS TABLE refresh_tokens WITH DATA';
    END IF;

    IF to_regclass('public.token_blacklist') IS NOT NULL THEN
        EXECUTE 'CREATE TABLE IF NOT EXISTS z_backup_token_blacklist AS TABLE token_blacklist WITH DATA';
    END IF;

    IF to_regclass('public.persistent_logins') IS NOT NULL THEN
        EXECUTE 'CREATE TABLE IF NOT EXISTS z_backup_persistent_logins AS TABLE persistent_logins WITH DATA';
    END IF;
END $$;

-- === DROP LEGACY TABLES ===
-- Refresh tokens (JWT flow) – not used with Spring Session
DROP TABLE IF EXISTS refresh_tokens CASCADE;

-- Access-token blacklist (JWT logout) – not used with Spring Session
DROP TABLE IF EXISTS token_blacklist CASCADE;

-- Spring Security remember-me tokens – only drop if you’re NOT using remember-me
DROP TABLE IF EXISTS persistent_logins CASCADE;

-- NOTE:
-- Do NOT drop SPRING_SESSION / SPRING_SESSION_ATTRIBUTES – those are used by Spring Session.
