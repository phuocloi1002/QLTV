package com.example.QLTV.service;

import com.example.QLTV.dto.request.StaffCreationRequest;
import com.example.QLTV.dto.request.StaffUpdateRequest;
import com.example.QLTV.dto.response.StaffResponse;
import com.example.QLTV.enity.Role;
import com.example.QLTV.enity.Staff;
import com.example.QLTV.enity.User;
import com.example.QLTV.enity.UserRole;
import com.example.QLTV.enity.enums.UserStatus;
import com.example.QLTV.exception.ApiException;
import com.example.QLTV.exception.ErrorCode;
import com.example.QLTV.repository.IStaffRepository;
import com.example.QLTV.repository.IUserRepository;
import com.example.QLTV.repository.IUserRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffServiceImpl implements IStaffService {

    IStaffRepository staffRepository;
    IUserRepository userRepository;
    IUserRoleRepository userRoleRepository;
    IRoleService roleService;
    PasswordEncoder passwordEncoder;
    FileStorageService fileStorageService;

    @Override
    @Transactional
    public StaffResponse createStaff(StaffCreationRequest request) {

        // 1. Check email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.USER_EXISTED);
        }

        // 2. Check staff code
        if (staffRepository.existsByStaffCode(request.getStaffCode())) {
            throw new ApiException(ErrorCode.STAFF_CODE_EXISTED);
        }

        // 3. Create user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .status(UserStatus.ACTIVE)
                .build();
        user = userRepository.save(user);

        // 4. Assign role
        String roleName = request.getRoleName() != null
                ? request.getRoleName()
                : "LIBRARIAN";

        Role role = roleService.getRoleEntityByName(roleName);

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();
        userRoleRepository.save(userRole);

        // 5. Create staff
        Staff staff = new Staff();
        staff.setStaffCode(request.getStaffCode());
        staff.setStatus(UserStatus.ACTIVE);
        staff.setUser(user);
        staff.setIsDeleted(false);

        return convertToStaffResponse(staffRepository.save(staff));
    }

    @Override
    public List<StaffResponse> findAllStaff() {
        return staffRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::convertToStaffResponse)
                .toList();
    }


    @Override
    @Transactional
    public StaffResponse updateStaff(String staffId, StaffUpdateRequest request) {

        Staff staff = getStaffEntityById(staffId);
        User user = staff.getUser();

        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());

        if (request.getIsDeleted() != null) {
            staff.setIsDeleted(request.getIsDeleted());
            user.setIsDeleted(request.getIsDeleted());
        }

        userRepository.save(user);

        staff.setStatus(request.getStatus());
        return convertToStaffResponse(staffRepository.save(staff));
    }

    @Override
    @Transactional
    public void deleteStaff(String staffId) {
        Staff staff = getStaffEntityById(staffId);
        staff.setIsDeleted(true);
        staffRepository.save(staff);
    }

    @Override
    public Staff getStaffEntityById(String staffId) {
        return staffRepository.findById(UUID.fromString(staffId))
                .orElseThrow(() -> new ApiException(ErrorCode.STAFF_NOT_EXISTED));
    }

    private StaffResponse convertToStaffResponse(Staff staff) {

        User user = staff.getUser();

        var builder = StaffResponse.builder()
                .staffId(staff.getId())
                .staffCode(staff.getStaffCode())
                .status(staff.getStatus())
                .createdAt(staff.getCreatedAt());

        if (user != null) {
            builder.email(user.getEmail())
                    .fullName(user.getFullName())
                    .phone(user.getPhone())
                    .avatar(user.getAvatar());

            if (user.getUserRoles() != null) {
                Set<String> roles = user.getUserRoles().stream()
                        .filter(ur -> ur.getRole() != null)
                        .map(ur -> ur.getRole().getName())
                        .collect(Collectors.toSet());
                builder.roles(roles);
            }
        } else {
            log.warn("Staff {} has no linked user!", staff.getId());
        }

        return builder.build();
    }

    @Override
    public StaffResponse getStaffResponseById(String staffId) {
        Staff staff = getStaffEntityById(staffId);

        return convertToStaffResponse(staff);
    }

    @Transactional
    public StaffResponse updateAvatar(String staffId, MultipartFile file) {
        // 1. Tìm Staff và User
        Staff staff = getStaffEntityById(staffId);
        User user = staff.getUser();

        // 2. Lưu file vật lý qua LocalFileStorageService
        // Trả về relative path (ví dụ: /images/uuid.png)
        String relativePath = fileStorageService.store(user.getId(), file);

        // 3. Cập nhật Database
        user.setAvatar(relativePath);
        userRepository.save(user);

        return convertToStaffResponse(staff);
    }


}
