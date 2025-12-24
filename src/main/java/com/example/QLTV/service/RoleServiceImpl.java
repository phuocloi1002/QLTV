package com.example.QLTV.service;

import com.example.QLTV.dto.request.RoleCreationRequest;
import com.example.QLTV.dto.response.RoleResponse;
import com.example.QLTV.enity.Permission;
import com.example.QLTV.enity.Role;
import com.example.QLTV.exception.ApiException;
import com.example.QLTV.exception.ErrorCode;
import com.example.QLTV.mapper.IRoleMapper;
import com.example.QLTV.repository.IPermissionRepository;
import com.example.QLTV.repository.IRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements IRoleService {

    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;
    IRoleMapper iRoleMapper;

    @Override
    @Transactional
    public RoleResponse createRole(RoleCreationRequest request) {

        if (roleRepository.existsByName(request.getName())) {
            throw new ApiException(ErrorCode.ROLE_NAME_EXISTED);
        }

        Set<Permission> permissions =
                validateAndFetchPermissions(request.getPermissionNames());

        Role role = roleRepository.save(
                Role.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .permissions(permissions)
                        .isDeleted(false)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return convertToRoleResponse(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> findAllRoles() {
        return roleRepository.findAllWithPermissions()
                .stream()
                .map(iRoleMapper::toRoleResponse)
                .toList();
    }

    @Override
    @Transactional
    public RoleResponse updateRole(String roleId, RoleCreationRequest request) {
        Role role = roleRepository.findById(UUID.fromString(roleId))
                .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));

        Set<Permission> permissions = validateAndFetchPermissions(request.getPermissionNames());

        role.setDescription(request.getDescription());
        role.setPermissions(permissions);
        role.setUpdatedAt(LocalDateTime.now());

        return iRoleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public void deleteRole(String roleId) {
        Role role = getRoleEntityById(roleId);
        role.setIsDeleted(true);
        roleRepository.save(role);
    }


    @Override
    public Role getRoleEntityById(String roleId) {
        return roleRepository.findById(UUID.fromString(roleId))
                .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
    }

    @Override
    public Role getRoleEntityByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
    }


    private Set<Permission> validateAndFetchPermissions(Set<String> permissionNames) {
        if (permissionNames == null || permissionNames.isEmpty()) {
            return new HashSet<>();
        }

        // Đảm bảo Repository của bạn đã đổi kiểu tìm kiếm sang String
        List<Permission> permissions = permissionRepository.findByNameIn(permissionNames);

        if (permissions.size() != permissionNames.size()) {
            throw new ApiException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        return new HashSet<>(permissions);
    }

    // Helper Method ánh xạ thủ công từ Entity sang DTO
    private RoleResponse convertToRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .permissions(
                        role.getPermissions().stream()
                                .map(Permission::getName)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    @Override
    public RoleResponse getRoleById(String roleId) {
        Role role = getRoleEntityById(roleId);

        return iRoleMapper.toRoleResponse(role);
    }
}