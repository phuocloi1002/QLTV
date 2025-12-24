package com.example.QLTV.controller;

import com.example.QLTV.ApiResponse;
import com.example.QLTV.dto.request.RoleCreationRequest;
import com.example.QLTV.dto.response.RoleResponse;
import com.example.QLTV.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {


    IRoleService roleService;


    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@RequestBody RoleCreationRequest request) {
        RoleResponse role = roleService.createRole(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleResponse>builder()
                        .code(1000)
                        .data(role)
                        .build());
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> findAllRoles() {
        List<RoleResponse> roles = roleService.findAllRoles();
        return ResponseEntity.ok(ApiResponse.<List<RoleResponse>>builder()
                .code(1000)
                .data(roles)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable String id) {
        RoleResponse role = roleService.getRoleById(id);
        return ResponseEntity.ok(ApiResponse.<RoleResponse>builder()
                .code(1000)
                .data(role)
                .build());
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable("roleId") String roleId,
            @RequestBody RoleCreationRequest request) {
        RoleResponse role = roleService.updateRole(roleId, request);
        return ResponseEntity.ok(ApiResponse.<RoleResponse>builder()
                .code(1000)
                .message("Role updated successfully")
                .data(role)
                .build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Trả về mã 204
    public void deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
    }
}