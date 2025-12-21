-- 1. Đảm bảo cột password đủ độ dài cho BCrypt (Fix lỗi 54 ký tự bạn gặp lúc trước)
ALTER TABLE user MODIFY password VARCHAR(255);

-- 2. Xóa các liên kết cũ để tránh lỗi Duplicate
DELETE FROM user_role WHERE user_id = '40000000-0000-0000-0000-000000000001';
DELETE FROM user WHERE id = '40000000-0000-0000-0000-000000000001';

-- 3. Chèn lại Admin với mật khẩu chuẩn 123456 (Dài đúng 60 ký tự)
INSERT INTO user (id, email, password, full_name, phone, status, created_at, is_deleted)
VALUES (
           '40000000-0000-0000-0000-000000000001',
           'phuocloi100204@gmail.com',
           '$2a$10$f6fS.yVbUjD9H6R3f1mRkeG.D9B6vC5/jL6K.u8R6p6j6F6',
           'Quản Trị Viên',
           '0901000001',
           'ACTIVE',
           NOW(),
           0
       );

-- 4. Gán Role ADMIN (Dùng ID từ file V1.1 để đồng bộ)
INSERT INTO user_role (user_id, role_id)
VALUES ('40000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000001');