package com.example.QLTV.controller;

import com.example.QLTV.ApiResponse;
import com.example.QLTV.dto.request.RoleCreationRequest;
import com.example.QLTV.dto.response.RoleResponse;
import com.example.QLTV.service.IRoleService;
import com.example.QLTV.util.JsonResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
        return JsonResponse.created(role, "Role created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> findAllRoles() {
        List<RoleResponse> roles = roleService.findAllRoles();
        return JsonResponse.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable String id) {
        RoleResponse role = roleService.getRoleById(id);
        return JsonResponse.ok(role);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable("roleId") String roleId,
            @RequestBody RoleCreationRequest request) {
        RoleResponse role = roleService.updateRole(roleId, request);
        return JsonResponse.ok(role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return JsonResponse.noContent();
    }
}