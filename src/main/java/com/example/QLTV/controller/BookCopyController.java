package com.example.QLTV.controller;

import com.example.QLTV.ApiResponse;
import com.example.QLTV.dto.request.BookCopyStatusRequest;
import com.example.QLTV.dto.response.BookCopyResponse;
import com.example.QLTV.service.IBookCopyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/book-copies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookCopyController {
    IBookCopyService bookCopyService;

    @GetMapping
    public ApiResponse<List<BookCopyResponse>> getCopiesByBook(@RequestParam UUID bookId) {
        return ApiResponse.<List<BookCopyResponse>>builder()
                .data(bookCopyService.getCopiesByBookId(bookId))
                .build();
    }

    @GetMapping("/{copyId}")
    public ApiResponse<BookCopyResponse> getCopyById(@PathVariable UUID copyId) {
        return ApiResponse.<BookCopyResponse>builder()
                .data(bookCopyService.getCopyById(copyId)) // Giả định Service đã có hàm này
                .build();
    }

    @PatchMapping("/{copyId}/circulation-status")
    public ApiResponse<BookCopyResponse> updateStatus(
            @PathVariable UUID copyId,
            @RequestBody @Valid BookCopyStatusRequest request) {
        return ApiResponse.<BookCopyResponse>builder()
                .data(bookCopyService.updateStatus(copyId, request))
                .build();
    }

    @DeleteMapping("/{copyId}")
    public ApiResponse<Void> deleteCopy(@PathVariable UUID copyId) {
        bookCopyService.deleteCopy(copyId);
        return ApiResponse.<Void>builder().build();
    }
}