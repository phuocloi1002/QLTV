package com.example.QLTV.repository;

import com.example.QLTV.enity.UserRole;
import com.example.QLTV.enity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    
}