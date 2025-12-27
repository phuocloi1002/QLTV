package com.example.QLTV.service;

import com.example.QLTV.dto.request.BookCopyStatusRequest;
import com.example.QLTV.dto.response.BookCopyResponse;
import java.util.List;
import java.util.UUID;

public interface IBookCopyService {
    BookCopyResponse updateStatus(UUID copyId, BookCopyStatusRequest request);
    BookCopyResponse getCopyById(UUID copyId);
    List<BookCopyResponse> getCopiesByBookId(UUID bookId);
    void deleteCopy(UUID copyId);
}