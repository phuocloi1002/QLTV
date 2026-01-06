package com.example.QLTV.mapper;

import com.example.QLTV.dto.request.StudentRequest;
import com.example.QLTV.dto.response.StudentResponse;
import com.example.QLTV.enity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IStudentMapper {
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phone")
    StudentResponse toStudentResponse(Student student);

    // Dùng để tạo mới
    @Mapping(target = "user.phone", source = "phoneNumber")
    Student toStudent(StudentRequest request);

    // Dùng để cập nhật (Giải quyết lỗi truyền nhiều đối số)
    @Mapping(target = "user.fullName", source = "fullName")
    @Mapping(target = "user.phone", source = "phoneNumber")
    void updateStudent(@MappingTarget Student student, StudentRequest request);
}