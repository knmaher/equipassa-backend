package com.equipassa.equipassa.repository;

import com.equipassa.equipassa.model.ToolImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolImageRepository extends JpaRepository<ToolImage, Long> {
}