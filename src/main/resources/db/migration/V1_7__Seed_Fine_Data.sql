-- 1. Tạo khoản phạt LATE_RETURN (Trả muộn) cho sinh viên 102190001
-- Dựa trên phiếu mượn OVERDUE đã tạo ở V1.6
INSERT INTO fine (id, amount, fine_type, status, student_id, loan_id, created_at, updated_at)
SELECT
    UUID(),
    30000.0,
    'LATE_RETURN',
    'UNPAID',
    '60000000-0000-0000-0000-000000000001', -- ID SV 102190001
    l.id,
    NOW(),
    NOW()
FROM loan l
WHERE l.status = 'OVERDUE'
    LIMIT 1;

-- 2. Tạo khoản phạt BOOK_DAMAGE (Làm hỏng sách) cho sinh viên SV2024001
INSERT INTO fine (id, amount, fine_type, status, student_id, loan_id, created_at, updated_at)
SELECT
    UUID(),
    50000.0,
    'BOOK_DAMAGE',
    'PARTIALLY_PAID',
    '141c2673-bc5b-41ee-b71b-e2323e499070', -- ID SV2024001
    l.id,
    NOW(),
    NOW()
FROM loan l
WHERE l.student_id = '141c2673-bc5b-41ee-b71b-e2323e499070'
    LIMIT 1;

-- 3. Cập nhật fine_balance cho các sinh viên để đồng bộ công nợ
UPDATE student
SET fine_balance = 30000.0
WHERE id = '60000000-0000-0000-0000-000000000001';

UPDATE student
SET fine_balance = 50000.0
WHERE id = '141c2673-bc5b-41ee-b71b-e2323e499070';