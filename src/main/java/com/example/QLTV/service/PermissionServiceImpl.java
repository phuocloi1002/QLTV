package com.example.QLTV.service;

import com.example.QLTV.dto.request.PermissionRequest;
import com.example.QLTV.dto.response.PermissionResponse;
import com.example.QLTV.enity.Permission;
import com.example.QLTV.exception.ApiException; // Import ApiException của bạn
import com.example.QLTV.exception.ErrorCode;   // Import ErrorCode của bạn
import com.example.QLTV.mapper.IPermissionMapper;
import com.example.QLTV.repository.IPermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements IPermissionService {
    IPermissionRepository permissionRepository;
    IPermissionMapper permissionMapper;

    @Override
    public PermissionResponse create(PermissionRequest request) {
        // Sử dụng ApiException thay vì RuntimeException
        if (permissionRepository.findByName(request.getName()).isPresent()) {
            throw new ApiException(ErrorCode.PERMISSION_EXISTED);
        }

        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public List<PermissionResponse> findAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    @Override
    public PermissionResponse update(UUID id, PermissionRequest request) {
        // Tìm permission, nếu không thấy trả về lỗi NOT_FOUND chuẩn API
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PERMISSION_NOT_FOUND));

        permission.setDescription(request.getDescription());
        permission.setName(request.getName());

        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public void delete(UUID id) {
        // Kiểm tra tồn tại trước khi xóa để báo lỗi chính xác
        if (!permissionRepository.existsById(id)) {
            throw new ApiException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        permissionRepository.deleteById(id);
    }
}