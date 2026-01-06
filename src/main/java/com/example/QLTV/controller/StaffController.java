package com.example.QLTV.controller;

import com.example.QLTV.ApiResponse;
import com.example.QLTV.dto.request.StaffCreationRequest;
import com.example.QLTV.dto.request.StaffUpdateRequest;
import com.example.QLTV.dto.response.StaffResponse;
import com.example.QLTV.service.IStaffService;
import com.example.QLTV.util.JsonResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {

    IStaffService staffService;

    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(@RequestBody StaffCreationRequest request) {
        StaffResponse staff = staffService.createStaff(request);
        return JsonResponse.created(staff, "Staff created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffResponse>>> findAllStaff() {
        List<StaffResponse> staffs = staffService.findAllStaff();
        return JsonResponse.ok(staffs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(@PathVariable String id) {
        StaffResponse staff = staffService.getStaffResponseById(id);
        return JsonResponse.ok(staff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
            @PathVariable String id,
            @RequestBody StaffUpdateRequest request) {
        StaffResponse staff = staffService.updateStaff(id, request);
        return JsonResponse.ok(staff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable String id) {
        staffService.deleteStaff(id);
        return JsonResponse.ok(null);
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StaffResponse>> uploadAvatar(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {

        StaffResponse response = staffService.updateAvatar(id, file);
        return JsonResponse.ok(response);
    }
}