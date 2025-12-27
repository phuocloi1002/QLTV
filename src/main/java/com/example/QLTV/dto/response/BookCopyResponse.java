package com.example.QLTV.dto.response;

import com.example.QLTV.enity.enums.BookCondition;
import com.example.QLTV.enity.enums.BookStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCopyResponse {
    UUID id;
    String barcode;
    BookStatus circulationStatus;
    BookCondition conditionStatus;
    String bookTitle;
}