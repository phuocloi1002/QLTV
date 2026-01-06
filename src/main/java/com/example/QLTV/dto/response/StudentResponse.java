package com.example.QLTV.dto.response;

import com.example.QLTV.enity.enums.UserStatus;
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
public class StudentResponse {
    UUID id;
    String studentCode;
    String faculty;
    String clazz;
    Double fineBalance;
    UserStatus status;

    String fullName;
    String email;
    String phoneNumber;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
