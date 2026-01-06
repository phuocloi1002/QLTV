package com.example.QLTV.controller;

import com.example.QLTV.ApiResponse;
import com.example.QLTV.dto.request.BookCopyStatusRequest;
import com.example.QLTV.dto.response.BookCopyResponse;
import com.example.QLTV.service.IBookCopyService;
import com.example.QLTV.util.JsonResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<BookCopyResponse>>> getCopiesByBook(@RequestParam UUID bookId) {
        List<BookCopyResponse> copies = bookCopyService.getCopiesByBookId(bookId);
        return JsonResponse.ok(copies);
    }

    @GetMapping("/{copyId}")
    public ResponseEntity<ApiResponse<BookCopyResponse>> getCopyById(@PathVariable UUID copyId) {
        BookCopyResponse copy = bookCopyService.getCopyById(copyId);
        return JsonResponse.ok(copy);
    }

    @PatchMapping("/{copyId}/circulation-status")
    public ResponseEntity<ApiResponse<BookCopyResponse>> updateStatus(
            @PathVariable UUID copyId,
            @RequestBody @Valid BookCopyStatusRequest request) {
        BookCopyResponse response = bookCopyService.updateStatus(copyId, request);
        return JsonResponse.ok(response);
    }

    @DeleteMapping("/{copyId}")
    public ResponseEntity<Void> deleteCopy(@PathVariable UUID copyId) {
        bookCopyService.deleteCopy(copyId);
        return JsonResponse.noContent();
    }
}