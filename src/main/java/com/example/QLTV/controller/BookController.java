package com.example.QLTV.controller;

import com.example.QLTV.ApiResponse;
import com.example.QLTV.dto.request.BookCreationRequest;
import com.example.QLTV.dto.request.BookUpdateRequest;
import com.example.QLTV.dto.response.BookResponse;
import com.example.QLTV.service.IBookService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    IBookService bookService;

    @GetMapping
    public ApiResponse<List<BookResponse>> getAllBooks() {
        return ApiResponse.<List<BookResponse>>builder()
                .data(bookService.findAllBooks())
                .build();
    }

    @PostMapping
    public ApiResponse<BookResponse> createBook(@RequestBody @Valid BookCreationRequest request) {
        return ApiResponse.<BookResponse>builder()
                .data(bookService.createBook(request))
                .build();
    }

    @GetMapping("/{bookId}")
    public ApiResponse<BookResponse> getBookDetail(@PathVariable UUID bookId) {
        return ApiResponse.<BookResponse>builder()
                .data(bookService.getBookById(bookId))
                .build();
    }


    @PutMapping("/{bookId}")
    public ApiResponse<BookResponse> updateBook(
            @PathVariable UUID bookId,
            @RequestBody @Valid BookUpdateRequest request) {
        return ApiResponse.<BookResponse>builder()
                .data(bookService.updateBook(bookId, request))
                .build();
    }

    @PutMapping("/{bookId}/shelf")
    public ApiResponse<BookResponse> updateShelf(
            @PathVariable UUID bookId,
            @RequestParam String shelfCode) {
        return ApiResponse.<BookResponse>builder()
                .data(bookService.updateShelf(bookId, shelfCode))
                .build();
    }

    @PatchMapping("/{bookId}/stock")
    public ApiResponse<BookResponse> updateStock(
            @PathVariable UUID bookId,
            @RequestParam int additionalQuantity) {
        return ApiResponse.<BookResponse>builder()
                .data(bookService.updateStock(bookId, additionalQuantity))
                .build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }
}