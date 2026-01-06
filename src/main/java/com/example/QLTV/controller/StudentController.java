package com.example.QLTV.controller;

import com.example.QLTV.ApiResponse;
import com.example.QLTV.dto.PageResponse;
import com.example.QLTV.dto.request.StudentRequest;
import com.example.QLTV.dto.request.StudentSearchRequest;
import com.example.QLTV.dto.response.BorrowResponse;
import com.example.QLTV.dto.response.StudentResponse;
import com.example.QLTV.enity.enums.UserStatus;
import com.example.QLTV.service.IStudentService;
import com.example.QLTV.util.JsonResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentController {

    IStudentService studentService;

    // 1. Liệt kê tài khoản sinh viên với bộ lọc và phân trang
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> getAllStudents(
            @ModelAttribute StudentSearchRequest request,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.<PageResponse<StudentResponse>>builder()
                        .code(1000)
                        .message("Success")
                        .data(new PageResponse<>(studentService.getAllStudents(request, pageable)))
                        .build()
        );
    }

    // 2. Tạo tài khoản sinh viên mới (Thủ thư tạo hồ sơ)
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(
            @RequestBody @Valid StudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        return JsonResponse.created(response, "Student profile created successfully.");
    }

    // 3. Xem chi tiết sinh viên (Thông tin cá nhân, trạng thái, công nợ)
    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentDetail(@PathVariable UUID studentId) {
        StudentResponse student = studentService.getStudentById(studentId);
        return JsonResponse.ok(student);
    }

    // 4. Cập nhật thông tin sinh viên
    @PutMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable UUID studentId,
            @RequestBody @Valid StudentRequest request) {
        StudentResponse response = studentService.updateStudent(studentId, request);
        return JsonResponse.ok(response);
    }

    // 5. Khóa/Mở khóa tài khoản (Thay đổi trạng thái)
    @PatchMapping("/{studentId}/status")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStatus(
            @PathVariable UUID studentId,
            @RequestParam UserStatus status) {
        StudentResponse response = studentService.updateStatus(studentId, status);
        return JsonResponse.ok(response);
    }

    // 6. Đặt lại mật khẩu (về mặc định là mã sinh viên)
    @PostMapping("/{studentId}/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@PathVariable UUID studentId) {
        studentService.resetPassword(studentId);
        return JsonResponse.ok(null);
    }

    // 7. Xem lịch sử mượn trả
    @GetMapping("/{studentId}/borrow-history")
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> getBorrowHistory(@PathVariable UUID studentId) {
        List<BorrowResponse> history = studentService.getBorrowHistory(studentId);
        return JsonResponse.ok(history);
    }

    // 8. Xem danh sách vi phạm & công nợ
    @GetMapping("/{studentId}/violations")
    public ResponseEntity<ApiResponse<StudentResponse>> getViolations(@PathVariable UUID studentId) {
        StudentResponse response = studentService.getViolationAndDebt(studentId);
        return JsonResponse.ok(response);
    }
}