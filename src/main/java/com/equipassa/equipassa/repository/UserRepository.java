package com.equipassa.equipassa.repository;

import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.model.UserRole;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
                SELECT u FROM User u
                LEFT JOIN FETCH u.address
                LEFT JOIN FETCH u.organization
                WHERE u.id = :id
            """)
    Optional<User> findByIdWithDetails(@Param("id") Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findFirstByOrganizationIdAndRole(Long organizationId, UserRole role);
}
