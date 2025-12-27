package com.example.QLTV.mapper;

import com.example.QLTV.dto.request.BookCreationRequest;
import com.example.QLTV.dto.request.BookUpdateRequest;
import com.example.QLTV.dto.response.BookResponse;
import com.example.QLTV.enity.Book;
import com.example.QLTV.enity.enums.BookStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IBookMapper {

    // Chuyển từ Request sang Entity để lưu vào DB
    Book toBook(BookCreationRequest request);

    // Cập nhật Entity từ Request (Dùng cho API PUT)
    void updateBook(@MappingTarget Book book, BookUpdateRequest request);

    // Chuyển từ Entity sang Response để trả về cho Client
    // Tự động tính toán số lượng bản in thông qua các phương thức default bên dưới
    @Mapping(target = "totalQuantity", expression = "java(calculateTotal(book))")
    @Mapping(target = "availableQuantity", expression = "java(calculateAvailable(book))")
    BookResponse toBookResponse(Book book);

    // Logic tính tổng số bản in (BookCopy)
    default Integer calculateTotal(Book book) {
        if (book.getCopies() == null) return 0;
        return book.getCopies().size();
    }

    // Logic tính số bản in có thể cho mượn (Status = AVAILABLE)
    default Integer calculateAvailable(Book book) {
        if (book.getCopies() == null) return 0;
        return (int) book.getCopies().stream()
                .filter(copy -> copy.getCirculationStatus() == BookStatus.AVAILABLE)
                .count();
    }
}