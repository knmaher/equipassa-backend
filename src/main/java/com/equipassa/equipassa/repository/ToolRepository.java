package com.equipassa.equipassa.repository;

import com.equipassa.equipassa.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    List<Tool> findByOrganizationId(Long organizationId);
}
