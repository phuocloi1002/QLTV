package com.example.QLTV.mapper;

import com.example.QLTV.dto.response.RoleResponse;
import com.example.QLTV.enity.Permission;
import com.example.QLTV.enity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IRoleMapper {

    @Mapping(target = "permissions", qualifiedByName = "mapPermissionsToNames")
    RoleResponse toRoleResponse(Role role);

    @Named("mapPermissionsToNames")
    default Set<String> mapPermissionsToNames(Set<Permission> permissions) {
        if (permissions == null) return null;
        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}