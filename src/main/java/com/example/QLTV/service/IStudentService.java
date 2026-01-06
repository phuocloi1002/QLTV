package com.example.QLTV.service;

import com.example.QLTV.dto.request.StudentRequest;
import com.example.QLTV.dto.request.StudentSearchRequest;
import com.example.QLTV.dto.response.BorrowResponse;
import com.example.QLTV.dto.response.StudentResponse;
import com.example.QLTV.enity.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IStudentService {
    // Tạo hồ sơ sinh viên mới (Thủ thư tạo)
    StudentResponse createStudent(StudentRequest request);

    // Lấy chi tiết sinh viên theo ID
    StudentResponse getStudentById(UUID id);

    // Lấy danh sách sinh viên có phân trang và bộ lọc
    Page<StudentResponse> getAllStudents(StudentSearchRequest request, Pageable pageable);
    // Cập nhật thông tin cá nhân sinh viên
    StudentResponse updateStudent(UUID id, StudentRequest request);

    // Khóa/Mở khóa tài khoản sinh viên
    StudentResponse updateStatus(UUID id, UserStatus status);

    // Đặt lại mật khẩu về mặc định và gửi thông báo
    void resetPassword(UUID id);

    // Xem lịch sử mượn trả của sinh viên
    List<BorrowResponse> getBorrowHistory(UUID studentId);

    // Xem danh sách vi phạm và công nợ (Fine/Debt)
    // Bạn có thể tạo thêm DTO riêng cho Violation nếu cần
    StudentResponse getViolationAndDebt(UUID studentId);
}