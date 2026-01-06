package com.example.QLTV.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {
     String studentCode;
     String fullName;
     String email;
     String phoneNumber;
     String faculty;
     String clazz;
}
