package com.example.QLTV.dto.response;

import com.example.QLTV.enity.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {

    UUID id;
    String name;
    String description;

    LocalDateTime createdAt;

    Set<String> permissions;

}