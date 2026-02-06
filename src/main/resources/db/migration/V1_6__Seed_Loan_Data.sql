-- 1. Chèn phiếu mượn cho SV2024001
INSERT INTO loan (id, borrowed_at, due_date, status, student_id, book_copy_id, created_at, updated_at)
SELECT
    UUID(),
    '2026-02-01 10:00:00',
    '2026-02-15 10:00:00',
    'BORROWING',
    '141c2673-bc5b-41ee-b71b-e2323e499070',
    (SELECT id FROM book_copy WHERE circulation_status = 'AVAILABLE' LIMIT 1),
    NOW(),
    NOW();

-- 2. Chèn phiếu mượn cho SV 102190001 (Quá hạn)
INSERT INTO loan (id, borrowed_at, due_date, status, student_id, book_copy_id, created_at, updated_at)
SELECT
    UUID(),
    '2026-01-01 08:30:00',
    '2026-01-15 08:30:00',
    'OVERDUE',
    '60000000-0000-0000-0000-000000000001',
    (SELECT id FROM book_copy WHERE circulation_status = 'AVAILABLE' LIMIT 1 OFFSET 1),
    NOW(),
    NOW();

-- 3. Cập nhật trạng thái sách (Dùng JOIN để tránh lỗi MySQL)
UPDATE book_copy bc
    INNER JOIN loan l ON bc.id = l.book_copy_id
SET bc.circulation_status = 'BORROWED'
WHERE l.status IN ('BORROWING', 'OVERDUE');