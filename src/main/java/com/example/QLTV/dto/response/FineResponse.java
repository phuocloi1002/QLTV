package com.example.QLTV.dto.response;

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
public class FineResponse {
    UUID id;
    Double amount;
    String fineType;
    String status;
    String bookTitle;
    LocalDateTime createdAt;
}
