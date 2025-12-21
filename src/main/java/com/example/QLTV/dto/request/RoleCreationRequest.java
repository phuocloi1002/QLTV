package com.example.QLTV.dto.request;

import com.example.QLTV.enity.enums.PermissionName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreationRequest {

    String name;

    String description;

    Set<PermissionName> permissionNames;
}