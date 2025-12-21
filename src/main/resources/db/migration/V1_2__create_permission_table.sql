

-- Tạo bảng với kiểu dữ liệu BINARY(16) (tối ưu cho UUID)
CREATE TABLE permission (
                            id CHAR(36) PRIMARY KEY,
                            name VARCHAR(50) NOT NULL UNIQUE,
                            description VARCHAR(255),
                            created_at DATETIME,
                            updated_at DATETIME,
                            created_by VARCHAR(36),
                            updated_by VARCHAR(36),
                            is_deleted BOOLEAN
);


INSERT INTO permission (id, name, description, is_deleted, created_at) VALUES
                                                                           (UUID(), 'ROLE_READ', 'Quyền xem danh sách vai trò', FALSE, NOW()),
                                                                           (UUID(), 'ROLE_CREATE', 'Quyền tạo vai trò mới', FALSE, NOW()),
                                                                           (UUID(), 'ROLE_UPDATE', 'Quyền cập nhật vai trò', FALSE, NOW()),
                                                                           (UUID(), 'ROLE_DELETE', 'Quyền xóa vai trò', FALSE, NOW()),
                                                                           (UUID(), 'BOOK_MANAGE', 'Quyền quản lý kho sách', FALSE, NOW()),
                                                                           (UUID(), 'STUDENT_MANAGE', 'Quyền quản lý sinh viên', FALSE, NOW());
