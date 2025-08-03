package com.equipassa.equipassa.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tools")
public class Tool extends Auditable {
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Column(name = "condition_status", nullable = false, length = 50)
    private String conditionStatus;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @OneToMany(mappedBy = "tool", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ToolImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getConditionStatus() {
        return conditionStatus;
    }

    public void setConditionStatus(final String conditionStatus) {
        this.conditionStatus = conditionStatus;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(final Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public List<ToolImage> getImages() {
        return images;
    }

    public void setImages(final List<ToolImage> images) {
        this.images = images;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(final Organization organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Tool tool)) return false;
        if (!super.equals(o)) return false; // includes Auditable's equality
        return Objects.equals(getName(), tool.getName()) &&
                Objects.equals(getCategory(), tool.getCategory()) &&
                Objects.equals(getConditionStatus(), tool.getConditionStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getCategory(), getConditionStatus());
    }

    @Override
    public String toString() {
        return "Tool{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", conditionStatus='" + conditionStatus + '\'' +
                ", quantityAvailable=" + quantityAvailable +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", createdBy=" + getCreatedBy() +
                ", updatedBy=" + getUpdatedBy() +
                ", version=" + getVersion() +
                '}';
    }
}
