package com.example.QLTV.controller;

import com.example.QLTV.ApiResponse;
import com.example.QLTV.dto.request.StaffCreationRequest;
import com.example.QLTV.dto.request.StaffUpdateRequest;
import com.example.QLTV.dto.response.StaffResponse;
import com.example.QLTV.service.IStaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {
    IStaffService staffService;
    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(
            @RequestBody StaffCreationRequest staffCreationRequest) {
        StaffResponse staff = staffService.createStaff(staffCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<StaffResponse>builder()
                        .code(1000)
                        .message("Staff created successfully")
                        .data(staff)
                        .build());
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffResponse>>> findAllStaff(){
        List<StaffResponse> staffs = staffService.findAllStaff();
        return ResponseEntity.ok(ApiResponse.<List<StaffResponse>>builder()
                        .code(1000)
                        .data(staffs)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(@PathVariable String id) {
        // Lưu ý: Bạn cần dùng hàm getStaffResponseById (trả về DTO)
        // thay vì getStaffEntityById (trả về Entity)
        StaffResponse staff = staffService.getStaffResponseById(id);

        return ResponseEntity.ok(ApiResponse.<StaffResponse>builder()
                .code(1000)
                .data(staff)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
            @PathVariable String id,
            @RequestBody StaffUpdateRequest request){
        StaffResponse staff = staffService.updateStaff(id, request);
        return ResponseEntity.ok(ApiResponse.<StaffResponse>builder()
                        .code(1000)
                        .message("Staff updated successfully")
                        .data(staff)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable String id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Staff has been deleted")
                .build());
    }
}
