package com.example.QLTV.dto.request;

import com.example.QLTV.enity.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentSearchRequest {
    String studentCode;
    String fullName;
    UserStatus status;
    String faculty;
}