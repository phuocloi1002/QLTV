package com.example.QLTV.repository;

import com.example.QLTV.enity.BookCopy;
import com.example.QLTV.enity.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IBookCopyRepository extends JpaRepository<BookCopy, UUID> {

    // Chỉ lấy danh sách bản sao của 1 đầu sách mà chưa bị xóa mềm
    List<BookCopy> findAllByBookIdAndIsDeletedFalse(UUID bookId);

    // Tìm theo mã vạch và phải chưa bị xóa
    Optional<BookCopy> findByBarcodeAndIsDeletedFalse(String barcode);

    // Đếm số lượng bản in đang sẵn sàng (chỉ tính các bản chưa bị xóa)
    long countByBookIdAndCirculationStatusAndIsDeletedFalse(UUID bookId, BookStatus status);
}