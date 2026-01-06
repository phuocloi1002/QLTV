package com.example.QLTV.repository;

import com.example.QLTV.enity.Student;
import com.example.QLTV.enity.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IStudentRepository extends JpaRepository<Student, UUID> {

    // SỬA LỖI: Đổi kiểu tham số từ Student thành String
    boolean existsByStudentCode(String studentCode);

    // SỬA LỖI: Đổi kiểu tham số từ Student thành String
    @EntityGraph(attributePaths = {"user"})
    Optional<Student> findByStudentCode(String studentCode);

    // API: Lấy danh sách sinh viên với bộ lọc (Mã số và Trạng thái)
    @Query("SELECT s FROM Student s JOIN s.user u " +
            "WHERE (:studentCode IS NULL OR s.studentCode LIKE %:studentCode%) " +
            "AND (:status IS NULL OR s.status = :status)")
    Page<Student> findAllWithFilter(
            @Param("studentCode") String studentCode,
            @Param("status") UserStatus status,
            Pageable pageable);

    // Tìm sinh viên theo Email (join sang bảng User)
    @Query("SELECT s FROM Student s WHERE s.user.email = :email")
    Optional<Student> findByEmail(@Param("email") String email);
}