ALTER TABLE tools
    ADD COLUMN organization_id BIGINT;

ALTER TABLE tools
    ADD CONSTRAINT fk_tools_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS idx_tools_organization_id ON tools (organization_id);
