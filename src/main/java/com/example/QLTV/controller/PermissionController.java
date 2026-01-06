package com.example.QLTV.controller;

import com.example.QLTV.ApiResponse;
import com.example.QLTV.dto.request.PermissionRequest;
import com.example.QLTV.dto.response.PermissionResponse;
import com.example.QLTV.service.IPermissionService;
import com.example.QLTV.util.JsonResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    IPermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> create(@RequestBody PermissionRequest request) {
        PermissionResponse permission = permissionService.create(request);
        return JsonResponse.created(permission, "Permission created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAll() {
        List<PermissionResponse> permissions = permissionService.findAll();
        return JsonResponse.ok(permissions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> update(
            @PathVariable UUID id,
            @RequestBody PermissionRequest request) {
        PermissionResponse permission = permissionService.update(id, request);
        return JsonResponse.ok(permission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        permissionService.delete(id);
        return JsonResponse.noContent();
    }
}