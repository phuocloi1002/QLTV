package com.example.QLTV.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCreationRequest {

    String staffCode;
    String email;
    String password;
    String fullName;
    String phone;
    String roleName;
}