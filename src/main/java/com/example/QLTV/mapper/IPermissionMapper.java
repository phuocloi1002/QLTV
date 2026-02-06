package com.example.QLTV.mapper;

import com.example.QLTV.dto.request.PermissionRequest;
import com.example.QLTV.dto.response.PermissionResponse;
import com.example.QLTV.enity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}