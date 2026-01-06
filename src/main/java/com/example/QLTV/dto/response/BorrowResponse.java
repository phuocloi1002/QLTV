package com.example.QLTV.dto.response;

import com.example.QLTV.enity.enums.BookStatus; // Hoặc Enum trạng thái mượn của bạn
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowResponse {
    UUID id;

    // Thông tin sách
    String bookTitle;
    String barcode;

    // Thông tin thời gian
    LocalDateTime borrowDate;
    LocalDateTime dueDate;
    LocalDateTime returnDate;

    // Trạng thái và công nợ
    String status;
    Double fineAmount;

}