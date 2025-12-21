package com.example.QLTV.repository;

import com.example.QLTV.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);


    Optional<User> findByEmail(String email);

    @Query(value = """
            SELECT * FROM user
            WHERE (:fullName IS NULL OR full_name LIKE CONCAT('%', :fullName, '%'))
              AND (:status IS NULL OR status = :status)
              AND is_deleted = FALSE
           """, nativeQuery = true)
    List<User> findByFilter(@Param("fullName") String fullName,
                            @Param("status") String status);
}