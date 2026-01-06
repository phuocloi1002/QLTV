package com.example.QLTV.util; // Đổi package cho đúng đồ án của bạn

import com.example.QLTV.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JsonResponse {

    // Trả về 200 OK
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return buildResponse(HttpStatus.OK, data, "Success");
    }

    // Trả về 201 Created
    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return buildResponse(HttpStatus.CREATED, data, "Created");
    }

    // Trả về 204 No Content (Thường dùng cho Delete thành công)
    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    // Helper builder để ánh xạ vào ApiResponse của đồ án
    private static <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus status, T data, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.<T>builder()
                        .code(1000) // Mã code thành công theo quy ước của bạn [cite: 224, 237]
                        .message(message)
                        .data(data)
                        .build()
                );
    }
}