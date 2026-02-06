package com.example.QLTV.mapper;

import com.example.QLTV.dto.response.StaffResponse;
import com.example.QLTV.enity.Staff;
import com.example.QLTV.enity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IStaffMapper {

    @Mapping(source = "id", target = "staffId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.userRoles", target = "roles")
    StaffResponse toResponse(Staff staff);

    default Set<String> map(List<UserRole> userRoles) {
        if (userRoles == null || userRoles.isEmpty()) {
            return Set.of();
        }

        return userRoles.stream()
                .filter(ur -> ur.getRole() != null)
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet());
    }

}

