package com.example.QLTV.service;

import com.example.QLTV.dto.request.StudentRequest;
import com.example.QLTV.dto.request.StudentSearchRequest;
import com.example.QLTV.dto.response.BorrowResponse;
import com.example.QLTV.dto.response.FineResponse;
import com.example.QLTV.dto.response.StudentResponse;
import com.example.QLTV.enity.Fine;
import com.example.QLTV.enity.Loan;
import com.example.QLTV.enity.Student;
import com.example.QLTV.enity.User;
import com.example.QLTV.enity.enums.UserStatus;
import com.example.QLTV.exception.ApiException;
import com.example.QLTV.exception.ErrorCode;
import com.example.QLTV.mapper.IFineMapper;
import com.example.QLTV.mapper.ILoanMapper;
import com.example.QLTV.mapper.IStudentMapper;
import com.example.QLTV.repository.IFineRepository;
import com.example.QLTV.repository.ILoanRepository;
import com.example.QLTV.repository.IStudentRepository;
import com.example.QLTV.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StudentServiceImpl implements IStudentService {
    ILoanRepository loanRepository;
    IStudentRepository studentRepository;
    IUserRepository userRepository;
    PasswordEncoder passwordEncoder;
    IStudentMapper studentMapper;
    EmailService emailService;
    ILoanMapper loanMapper;
    IFineRepository fineRepository;
    IFineMapper fineMapper;

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
//        user = userRepository.save(user);

        Student student = Student.builder()
                .studentCode(request.getStudentCode())
                .faculty(request.getFaculty())
                .clazz(request.getClazz())
                .status(UserStatus.INACTIVE)
                .fineBalance(0.0)
                .user(user)
                .build();

        Student savedStudent = studentRepository.save(student);

        emailService.sendActivationEmail(savedStudent);

        return convertToStudentResponse(savedStudent);


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
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.STUDENT_NOT_FOUND));

        studentMapper.updateStudent(student, request);

        student.setUpdatedAt(LocalDateTime.now());

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
        user.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);

        emailService.sendResetPasswordEmail(student);

        log.info("Đã đặt lại mật khẩu cho sinh viên: {}", student.getStudentCode());
    }

    @Override
    public List<BorrowResponse> getBorrowHistory(UUID studentId) {
        List<Loan> loans = loanRepository.findAllByStudentIdCustom(studentId);
        return loans.stream().map(loanMapper::toBorrowResponse).toList();
    }

    @Override
    public StudentResponse getViolationAndDebt(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ApiException(ErrorCode.STUDENT_NOT_FOUND));

        List<Fine> fines = fineRepository.findAllByStudentId(studentId);
        List<FineResponse> fineResponses = fineMapper.toFineResponseList(fines);

        StudentResponse response = studentMapper.toStudentResponse(student);
        response.setViolations(fineResponses);

        return response;
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