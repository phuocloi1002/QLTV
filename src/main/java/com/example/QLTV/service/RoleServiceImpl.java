package com.example.QLTV.service;

import com.example.QLTV.dto.request.RoleCreationRequest;
import com.example.QLTV.dto.response.RoleResponse;
import com.example.QLTV.enity.Permission;
import com.example.QLTV.enity.Role;
import com.example.QLTV.enity.enums.PermissionName;
import com.example.QLTV.enity.enums.RoleName;
import com.example.QLTV.exception.ApiException;
import com.example.QLTV.exception.ErrorCode;
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

    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;



    @Override
    @Transactional
    public RoleResponse createRole(RoleCreationRequest request) {

        RoleName roleName;
        try {
            roleName = RoleName.valueOf(request.getName());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.INVALID_ROLE_NAME);
        }

        if (roleRepository.existsByName(roleName)) {
            throw new ApiException(ErrorCode.ROLE_NAME_EXISTED);
        }

        Set<Permission> permissions =
                validateAndFetchPermissions(request.getPermissionNames());

        Role newRole = roleRepository.save(
                Role.builder()
                        .name(roleName)
                        .description(request.getDescription())
                        .permissions(permissions)
                        .isDeleted(false)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return convertToRoleResponse(newRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> findAllRoles() {
        return roleRepository.findAllWithPermissions()
                .stream()
                .map(this::convertToRoleResponse)
                .toList();
    }

    @Override
    @Transactional
    public RoleResponse updateRole(String roleId, RoleCreationRequest request) {
        // 1. Tìm Role hiện tại
        Role role = roleRepository.findById(UUID.fromString(roleId))
                .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));

        // 2. Lấy bộ Entity Permission mới dựa trên Enum gửi lên
        Set<Permission> permissions = validateAndFetchPermissions(request.getPermissionNames());

        // 3. Cập nhật thông tin
        role.setDescription(request.getDescription());
        role.setPermissions(permissions);
        role.setUpdatedAt(LocalDateTime.now());

        // 4. Lưu lại (JPA Dirty Checking sẽ tự động update bảng role_permission)
        return convertToRoleResponse(roleRepository.save(role));
    }

    @Transactional
    public RoleResponse deleteRole(String roleId) {
        Role role = roleRepository.findById(UUID.fromString(roleId))
                .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));

        // Thực hiện xóa mềm
        role.setIsDeleted(true);
        role.setUpdatedAt(LocalDateTime.now());

        // Lưu và trả về kết quả đã convert sang DTO
        return convertToRoleResponse(roleRepository.save(role));
    }


    @Override
    public Role getRoleEntityById(String roleId) {
        try {
            return roleRepository.findById(UUID.fromString(roleId))
                    .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
        } catch (IllegalArgumentException e) {
            // Xử lý khi chuỗi ID không đúng định dạng UUID
            throw new ApiException(ErrorCode.INVALID_PARAM);
        }
    }

    @Override
    public Role getRoleEntityByName(String roleName) {
        RoleName enumRoleName;
        try {
            enumRoleName = RoleName.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            // Bắt ngoại lệ nếu chuỗi không hợp lệ
            throw new ApiException(ErrorCode.ROLE_NOT_FOUND);
        }

        return roleRepository.findByName(enumRoleName)
                .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
    }

    // Tối ưu hóa N+1 Query (Tìm tất cả permissions trong 1 truy vấn)
    private Set<Permission> validateAndFetchPermissions(Set<PermissionName> permissionNames) {
        if (permissionNames == null || permissionNames.isEmpty()) {
            return new HashSet<>();
        }

        // 1. Tìm tất cả Permission Entity dựa trên danh sách Enum gửi lên
        // Đảm bảo IPermissionRepository của bạn đã đổi sang: findByNameIn(Set<PermissionName> names)
        List<Permission> foundPermissionsList = permissionRepository.findByNameIn(permissionNames);

        // 2. Kiểm tra số lượng tìm thấy có khớp với số lượng yêu cầu không
        if (foundPermissionsList.size() != permissionNames.size()) {
            // Lỗi này xảy ra nếu client gửi một PermissionName hợp lệ trong Enum
            // nhưng chưa được khởi tạo (Insert) vào bảng 'permission' trong DB
            throw new ApiException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        return new HashSet<>(foundPermissionsList);
    }

    // Helper Method ánh xạ thủ công từ Entity sang DTO
    private RoleResponse convertToRoleResponse(Role role) {
        if (role == null) return null;

        // 1. Ánh xạ từ Set<Permission> (Entity) sang Set<String> (DTO)
        Set<String> permissionNames = role.getPermissions() != null ?
                role.getPermissions().stream()
                        .map(p -> p.getName().name()) // Chuyển Enum PermissionName -> String
                        .collect(Collectors.toSet())
                : Set.of();

        // 2. Sử dụng Builder Pattern để tạo RoleResponse
        return RoleResponse.builder()
                .id(role.getId())
                // Chuyển Enum RoleName -> String để hiển thị cho Client
                .name(role.getName() != null ? role.getName().name() : null)
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .permissions(permissionNames)
                .build();
    }
}