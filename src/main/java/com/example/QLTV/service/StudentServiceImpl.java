package com.example.QLTV.service;

import com.example.QLTV.dto.request.StudentRequest;
import com.example.QLTV.dto.request.StudentSearchRequest;
import com.example.QLTV.dto.response.BorrowResponse;
import com.example.QLTV.dto.response.StudentResponse;
import com.example.QLTV.enity.Student;
import com.example.QLTV.enity.User;
import com.example.QLTV.enity.enums.UserStatus;
import com.example.QLTV.exception.ApiException;
import com.example.QLTV.exception.ErrorCode;
import com.example.QLTV.mapper.IStudentMapper;
import com.example.QLTV.repository.IStudentRepository;
import com.example.QLTV.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentServiceImpl implements IStudentService {

    IStudentRepository studentRepository;
    IUserRepository userRepository;
    PasswordEncoder passwordEncoder;
    IStudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.USER_EXISTED);
        }
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new ApiException(ErrorCode.STUDENT_CODE_EXISTED);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getStudentCode()))
                .fullName(request.getFullName())
                .phone(request.getPhoneNumber())
                .status(UserStatus.INACTIVE)
                .build();
        user = userRepository.save(user);

        Student student = Student.builder()
                .studentCode(request.getStudentCode())
                .faculty(request.getFaculty())
                .clazz(request.getClazz())
                .status(UserStatus.INACTIVE)
                .fineBalance(0.0)
                .user(user)
                .build();

        return convertToStudentResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse getStudentById(UUID id) {
        return studentRepository.findById(id)
                .map(this::convertToStudentResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.STUDENT_NOT_FOUND));
    }

    @Override
    public Page<StudentResponse> getAllStudents(StudentSearchRequest request, Pageable pageable) {
        return studentRepository.findAllWithFilter(
                request.getStudentCode(),
                request.getStatus(),
                pageable
        ).map(studentMapper::toStudentResponse);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(UUID id, StudentRequest request) {
        // 1. Tìm student từ DB
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.STUDENT_NOT_FOUND));

        // 2. Sử dụng mapper để cập nhật dữ liệu từ request vào student
        // Lưu ý: Gọi đúng hàm updateStudent có @MappingTarget
        studentMapper.updateStudent(student, request);

        // 3. Cập nhật thời gian
        student.setUpdatedAt(LocalDateTime.now());

        // 4. Lưu vào DB (Hàm save chỉ nhận 1 đối số là 'student')
        return studentMapper.toStudentResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public StudentResponse updateStatus(UUID id, UserStatus status) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.STUDENT_NOT_FOUND));

        student.setStatus(status);
        student.getUser().setStatus(status);
        student.setUpdatedAt(LocalDateTime.now());

        return convertToStudentResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void resetPassword(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.STUDENT_NOT_FOUND));

        User user = student.getUser();
        user.setPassword(passwordEncoder.encode(student.getStudentCode()));
        userRepository.save(user);
    }

    @Override
    public List<BorrowResponse> getBorrowHistory(UUID studentId) {
        // Sau này bạn sẽ tiêm IBorrowingRepository để lấy dữ liệu thực tế tại đây
        return Collections.emptyList();
    }

    @Override
    public StudentResponse getViolationAndDebt(UUID studentId) {
        return studentRepository.findById(studentId)
                .map(this::convertToStudentResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.STUDENT_NOT_FOUND));
    }

    private StudentResponse convertToStudentResponse(Student student) {
        User user = student.getUser();
        return StudentResponse.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .faculty(student.getFaculty())
                .clazz(student.getClazz())
                .fineBalance(student.getFineBalance())
                .status(student.getStatus())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}