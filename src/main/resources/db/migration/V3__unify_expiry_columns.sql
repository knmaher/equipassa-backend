alter TABLE users
    ADD COLUMN IF NOT EXISTS subscription_tier VARCHAR(50);

alter TABLE users
    alter COLUMN subscription_tier TYPE VARCHAR(50);

ALTER TABLE users ADD COLUMN expiry_date DATE;

ALTER TABLE organizations ADD COLUMN expiry_date DATE;

