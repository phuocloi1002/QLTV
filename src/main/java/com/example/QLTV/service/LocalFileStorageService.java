package com.example.QLTV.service;

import com.example.QLTV.exception.ApiException;
import com.example.QLTV.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalFileStorageService implements FileStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    private static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5MB

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Lưu file ảnh với tên {ownerId}.{ext}. Nếu đã có file cũ cùng ownerId,
     * sau khi ghi file mới sẽ dọn dẹp file cũ để tránh rác.
     */
    @Override
    public String store(UUID ownerId, MultipartFile file) {
        validateFile(file);

        String extension = extractExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueName = ownerId + extension;

        Path destinationDir = Paths.get(uploadDir);
        Path destinationFile = destinationDir.resolve(uniqueName).normalize();

        try {
            Files.createDirectories(destinationDir);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            cleanupOldFiles(destinationDir, ownerId.toString(), uniqueName);
        } catch (IOException ex) {
            log.error("Failed to store file {}", destinationFile, ex);
            throw new ApiException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        return "/images/" + uniqueName;
    }

    /**
     * Xoá file dựa trên đường dẫn tương đối đã lưu trong DB.
     */
    @Override
    public void delete(String relativePath) {
        if (!StringUtils.hasText(relativePath)) {
            return;
        }

        // relativePath bắt đầu bằng /images/, cần chuyển về đường dẫn vật lý
        String normalized = relativePath.replaceFirst("^/images/?", "");
        Path target = Paths.get(uploadDir, normalized);

        try {
            Files.deleteIfExists(target);
        } catch (IOException ex) {
            log.warn("Could not delete file {}", target, ex);
        }
    }

    /**
     * Kiểm tra các điều kiện cơ bản của file: tồn tại, mime type, kích thước.
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(ErrorCode.FILE_EMPTY);
        }

        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new ApiException(ErrorCode.FILE_INVALID_TYPE);
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ApiException(ErrorCode.FILE_TOO_LARGE);
        }
    }

    private String extractExtension(String originalFilename) {
        String cleaned = StringUtils.cleanPath(originalFilename);
        int dotIndex = cleaned.lastIndexOf('.');
        if (dotIndex == -1) {
            throw new ApiException(ErrorCode.FILE_EXTENSION_MISSING);
        }
        return cleaned.substring(dotIndex).toLowerCase();
    }

    /**
     * Xoá các file cũ của cùng ownerId (khác tên hiện tại) để tránh lưu nhiều phiên bản.
     */
    private void cleanupOldFiles(Path directory, String ownerId, String currentFileName) throws IOException {
        if (Files.notExists(directory)) {
            return;
        }
        try (Stream<Path> paths = Files.list(directory)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> {
                        String filename = path.getFileName().toString();
                        return filename.startsWith(ownerId) && !filename.equals(currentFileName);
                    })
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ex) {
                            log.warn("Could not delete old avatar {}", path, ex);
                        }
                    });
        }
    }
}
