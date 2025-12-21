package com.example.QLTV.repository;

import com.example.QLTV.enity.Permission;
import com.example.QLTV.enity.enums.PermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface IPermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(PermissionName name);
    List<Permission> findByNameIn(Collection<PermissionName> names);
}
