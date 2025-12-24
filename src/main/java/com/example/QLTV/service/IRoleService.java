package com.example.QLTV.service;

import com.example.QLTV.dto.request.RoleCreationRequest;
import com.example.QLTV.dto.response.RoleResponse;
import com.example.QLTV.enity.Role;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
    RoleResponse createRole(RoleCreationRequest request);

    List<RoleResponse> findAllRoles();

    RoleResponse updateRole(String roleId,RoleCreationRequest request);

    void  deleteRole(String roleId);

    Role getRoleEntityById(String roleId);

    Role getRoleEntityByName(String roleName);

    RoleResponse getRoleById(String roleId);

}
