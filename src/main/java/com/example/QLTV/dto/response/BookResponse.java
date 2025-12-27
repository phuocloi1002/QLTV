package com.example.QLTV.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
    UUID id;
    String title;
    String author;
    String isbn;
    String category;
    String publisher;
    String publishedYear;
    Double price;
    String shelfCode;

    Integer totalQuantity;     // Tổng số bản in đang có trong kho
    Integer availableQuantity; // Số bản in sẵn sàng cho mượn (Status = AVAILABLE)

    LocalDateTime createdAt;
    String createdBy;
}