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

    List<BookCopy> findAllByBookId(UUID bookId);

    // Tìm theo mã vạch (Barcode)
    Optional<BookCopy> findByBarcode(String barcode);

    // ĐẾM số lượng bản in của 1 đầu sách theo trạng thái lưu thông
    // Spring sẽ hiểu là: WHERE book_id = ? AND circulation_status = ?
    long countByBookIdAndCirculationStatus(UUID bookId, BookStatus circulationStatus);
}