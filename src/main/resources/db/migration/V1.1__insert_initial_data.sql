


INSERT INTO role (id, name, description, created_at, is_deleted) VALUES
                                                                     ('30000000-0000-0000-0000-000000000001', 'ADMIN', 'Quản trị viên hệ thống', NOW(), 0),
                                                                     ('30000000-0000-0000-0000-000000000002', 'LIBRARIAN', 'Thủ thư/Nhân viên thư viện', NOW(), 0),
                                                                     ('30000000-0000-0000-0000-000000000003', 'STUDENT', 'Sinh viên/Độc giả', NOW(), 0);


-- 1. ADMIN
INSERT INTO user (id, email, password, full_name, phone, status, created_at, is_deleted) VALUES
    ('40000000-0000-0000-0000-000000000001',
     'phuocloi100204@gmail.com',
     '$2a$10$f6fS.yVbUjD9H6R3f1mRkeG.D9B6vC5/jL6K.u8R6p6j6F6', -- BCrypt của '123456'
     'Quản Trị Viên',
     '0901000001',
     'ACTIVE',
     NOW(),
     0);
INSERT INTO user_role (user_id, role_id) VALUES
    ('40000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000001');

-- 2. STAFF (Thủ thư)
-- Thay thế mật khẩu giả lập bằng mã băm BCrypt của chuỗi '123456'
-- Mã băm: $2a$10$8.UnVuG9HHgUenSIVJyLTeGVGWnIy5SX7BnZxZ9SuLVzZAdE7le8u
INSERT INTO user (id, email, password, full_name, phone, status, created_at, is_deleted) VALUES
    ('40000000-0000-0000-0000-000000000002',
     'staff@dut.udn.vn',
     '$2a$10$8.UnVuG9HHgUenSIVJyLTeGVGWnIy5SX7BnZxZ9SuLVzZAdE7le8u',
     'Nguyễn Thị Thủ Thư',
     '0901000002',
     'ACTIVE',
     NOW(),
     0);

-- Gán quyền LIBRARIAN (Thủ thư) cho User vừa tạo
INSERT INTO user_role (user_id, role_id) VALUES
    ('40000000-0000-0000-0000-000000000002',
     '30000000-0000-0000-0000-000000000002');

-- Tạo thông tin Staff liên kết với User qua user_id
INSERT INTO staff (id, user_id, staff_code, status, created_at, is_deleted) VALUES
    ('50000000-0000-0000-0000-000000000001',
     '40000000-0000-0000-0000-000000000002',
     'LIB-001',
     'ACTIVE',
     NOW(),
     0);
-- 3. STUDENT (Sinh viên)
INSERT INTO user (id, email, password, full_name, phone, status, created_at, is_deleted) VALUES
    ('40000000-0000-0000-0000-000000000003', 'student@dut.udn.vn', '$2a$10$HASHED_STUDENT_PASSWORD', 'Trần Văn B', '0901000003', 'ACTIVE', NOW(), 0);
INSERT INTO user_role (user_id, role_id) VALUES
    ('40000000-0000-0000-0000-000000000003', '30000000-0000-0000-0000-000000000003');

INSERT INTO student (id, user_id, student_code, faculty, clazz, status, fine_balance, created_at, is_deleted) VALUES
    ('60000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000003', '102190001', 'CNTT', '19T1', 'ACTIVE', 0.0, NOW(), 0);



INSERT INTO book (id, title, author, isbn, category, publisher, published_year, price, shelf_code, created_at, is_deleted) VALUES
                                                                                                                               ('10000000-0000-0000-0000-000000000001', 'Cơ sở dữ liệu căn bản', 'Nguyễn Văn A', '978-604-0-12345-6', 'Khoa học máy tính', 'NXB Giáo dục', '2023', 150000.00, 'A-CNTT-K1', NOW(), 0),
                                                                                                                               ('10000000-0000-0000-0000-000000000002', 'Giải tích 1', 'Lê Thị C', '978-604-0-12345-7', 'Toán học', 'NXB Bách Khoa', '2020', 120000.00, 'A-TOAN-K1', NOW(), 0);



INSERT INTO book_copy (id, book_id, barcode, circulation_status, condition_status, created_at, is_deleted) VALUES
                                                                                                               ('11000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'BC-CSDL-001', 'AVAILABLE', 'GOOD', NOW(), 0), -- Có sẵn
                                                                                                               ('11000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'BC-CSDL-002', 'AVAILABLE', 'NEW', NOW(), 0), -- Có sẵn
                                                                                                               ('11000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000002', 'BC-GT1-001', 'AVAILABLE', 'FAIR', NOW(), 0); -- Có sẵn