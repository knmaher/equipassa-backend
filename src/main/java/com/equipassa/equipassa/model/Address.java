package com.equipassa.equipassa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "addresses")
public class Address extends Auditable {

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "country", nullable = false)
    private String country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        if (!super.equals(o)) return false; // includes Auditable's equality
        return Objects.equals(getStreet(), address.getStreet()) &&
                Objects.equals(getCity(), address.getCity()) &&
                Objects.equals(getState(), address.getState()) &&
                Objects.equals(getPostalCode(), address.getPostalCode()) &&
                Objects.equals(getCountry(), address.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStreet(), getCity(), getState(), getPostalCode(), getCountry());
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + getId() +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", createdBy=" + getCreatedBy() +
                ", updatedBy=" + getUpdatedBy() +
                ", version=" + getVersion() +
                '}';
    }
}
