package com.example.QLTV.repository;

import com.example.QLTV.enity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ILoanRepository extends JpaRepository<Loan, UUID> {
    @Query("SELECT l FROM Loan l " +
            "JOIN FETCH l.bookCopy bc " +
            "JOIN FETCH bc.book b " +
            "LEFT JOIN FETCH l.fines f " +
            "WHERE l.student.id = :studentId " +
            "ORDER BY l.borrowedAt DESC")
    List<Loan> findAllByStudentIdCustom(@Param("studentId") UUID studentId);
}
