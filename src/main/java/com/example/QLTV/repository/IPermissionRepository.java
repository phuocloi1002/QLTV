package com.example.QLTV.repository;

import com.example.QLTV.enity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(String name);

    boolean existsByName(String name);

    List<Permission> findByNameIn(Collection<String> names);
}