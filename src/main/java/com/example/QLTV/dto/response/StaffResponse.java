package com.example.QLTV.dto.response;

import com.example.QLTV.enity.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffResponse {

    UUID staffId;
    String staffCode;
    String email;
    String fullName;
    String phone;
    UserStatus status;

    LocalDateTime createdAt;

    Set<String> roles;
    String avatar;
}
