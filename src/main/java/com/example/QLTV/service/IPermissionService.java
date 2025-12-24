package com.example.QLTV.service;

import com.example.QLTV.dto.request.PermissionRequest;
import com.example.QLTV.dto.response.PermissionResponse;

import java.util.List;
import java.util.UUID;

public interface IPermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> findAll();

    // Bổ sung cập nhật mô tả quyền
    PermissionResponse update(UUID id, PermissionRequest request);

    void delete(UUID id);
}