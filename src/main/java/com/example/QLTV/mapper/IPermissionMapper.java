package com.example.QLTV.mapper;

import com.example.QLTV.dto.request.PermissionRequest;
import com.example.QLTV.dto.response.PermissionResponse;
import com.example.QLTV.enity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {

    // Ánh xạ từ Request (Client gửi lên) sang Entity (Để lưu DB)
    // Chúng ta ignore 'id' và 'roles' vì 'id' tự sinh, còn 'roles' xử lý ở phía Role
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Permission toPermission(PermissionRequest request);

    // Ánh xạ từ Entity (DB) sang Response (Trả về Client)
    PermissionResponse toPermissionResponse(Permission permission);
}