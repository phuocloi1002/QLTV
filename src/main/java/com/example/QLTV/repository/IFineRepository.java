package com.example.QLTV.repository;

import com.example.QLTV.enity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface IFineRepository extends JpaRepository<Fine, UUID> {
    @Query("SELECT f FROM Fine f " +
            "JOIN FETCH f.loan l " +
            "JOIN FETCH l.bookCopy bc " +
            "JOIN FETCH bc.book b " +
            "WHERE l.student.id = :studentId")
    List<Fine> findAllByStudentId(UUID studentId);

}
