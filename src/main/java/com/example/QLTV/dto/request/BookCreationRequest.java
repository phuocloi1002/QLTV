package com.example.QLTV.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCreationRequest {
    String title;

    String author;

    String isbn;

    String category;
    String publisher;
    String publishedYear;

    Double price;

    String shelfCode;

    Integer quantity;
}
