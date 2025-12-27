package com.example.QLTV.service;

import com.example.QLTV.dto.request.BookCreationRequest;
import com.example.QLTV.dto.request.BookUpdateRequest;
import com.example.QLTV.dto.response.BookResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface IBookService {
    // Tạo đầu sách mới và tự động sinh n bản in (BookCopy)
    BookResponse createBook(BookCreationRequest request);

    // Lấy danh sách đầu sách (có thể mở rộng thêm Filter sau này)
    List<BookResponse> findAllBooks();

    // Xem chi tiết đầu sách (bao gồm thông tin bản in)
    BookResponse getBookById(UUID bookId);

    // Cập nhật metadata: tựa, tác giả, mô tả...
    BookResponse updateBook(UUID bookId, BookUpdateRequest request);

    // Gán hoặc thay đổi khu vực/kệ mặc định (PUT /shelf)
    BookResponse updateShelf(UUID bookId, String shelfCode);

    // Xóa đầu sách (Chỉ khi các bản sao chưa được mượn)
    void deleteBook(UUID bookId);

    // Điều chỉnh tổng số bản in (PATCH /stock)
    BookResponse updateStock(UUID bookId, int additionalQuantity);
}