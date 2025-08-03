package com.equipassa.equipassa.repository;

import com.equipassa.equipassa.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByName(String name);
}
