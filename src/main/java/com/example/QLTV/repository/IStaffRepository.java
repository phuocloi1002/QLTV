package com.example.QLTV.repository;

import com.example.QLTV.enity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IStaffRepository extends JpaRepository<Staff, UUID> {

    boolean existsByStaffCode(String staffCode);

    Optional<Staff> findByUserId(UUID userId);

    List<Staff> findAllByIsDeletedFalse();
}