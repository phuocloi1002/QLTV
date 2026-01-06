package com.example.QLTV.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface  FileStorageService {
    /**
     * Lưu file xuống thư mục cấu hình và trả về đường dẫn tương đối (ví dụ: /images/2025/uuid.png).
     *
     * @param ownerId id của entity (ví dụ: employee) để phục vụ đặt tên file
     * @param file multipart file cần lưu
     * @return đường dẫn tương đối để lưu vào database
     */
    String store(UUID ownerId, MultipartFile file);

    /**
     * Xoá file theo đường dẫn tương đối (nếu tồn tại). Không throw lỗi nếu không tìm thấy file.
     *
     * @param relativePath ví dụ: /images/2025/uuid.png
     */
    void delete(String relativePath);
}