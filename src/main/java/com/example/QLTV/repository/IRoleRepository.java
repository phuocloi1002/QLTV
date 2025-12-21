package com.example.QLTV.repository;

import com.example.QLTV.enity.Role;
import com.example.QLTV.enity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID> {
    @Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.permissions")
    List<Role> findAllWithPermissions();
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
