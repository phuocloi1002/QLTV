package com.example.QLTV.service;

import com.example.QLTV.dto.request.StaffCreationRequest;
import com.example.QLTV.dto.request.StaffUpdateRequest;
import com.example.QLTV.dto.response.StaffResponse;
import com.example.QLTV.enity.Staff;

import java.util.List;
import java.util.UUID;

public interface IStaffService {
    StaffResponse createStaff(StaffCreationRequest request);

    List<StaffResponse> findAllStaff();

    StaffResponse updateStaff(String staffId,StaffUpdateRequest request);

    void deleteStaff(String staffId);

    Staff getStaffEntityById(String staffId);

    StaffResponse getStaffResponseById(String staffId);
}
