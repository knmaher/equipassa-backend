package com.equipassa.equipassa.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tool_images")
public class ToolImage extends Auditable {
    @Column(name = "s3_key", length = 255, nullable = false)
    private String s3Key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(final String s3Key) {
        this.s3Key = s3Key;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(final Tool tool) {
        this.tool = tool;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final ToolImage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getS3Key(), that.getS3Key());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getS3Key());
    }

    @Override
    public String toString() {
        return "ToolImage{" +
                "id=" + getId() +
                ", s3Key='" + s3Key + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", createdBy=" + getCreatedBy() +
                ", updatedBy=" + getUpdatedBy() +
                ", version=" + getVersion() +
                '}';
    }
}