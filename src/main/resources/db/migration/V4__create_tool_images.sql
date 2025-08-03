CREATE TABLE tool_images (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    updated_by VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    s3_key VARCHAR(255) NOT NULL,
    tool_id BIGINT NOT NULL REFERENCES tools (id) ON DELETE CASCADE
);

CREATE INDEX idx_tool_images_tool_id ON tool_images (tool_id);

ALTER TABLE tools DROP COLUMN image_url;