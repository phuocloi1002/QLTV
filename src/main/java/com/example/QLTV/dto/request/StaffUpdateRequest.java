package com.example.QLTV.dto.request;

import com.example.QLTV.enity.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffUpdateRequest {

    String fullName;
    String phone;

    UserStatus status;
    @JsonProperty("isDeleted")
    Boolean isDeleted;
}