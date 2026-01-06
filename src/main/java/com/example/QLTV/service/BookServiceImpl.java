package com.example.QLTV.service;

import com.example.QLTV.dto.request.BookCreationRequest;
import com.example.QLTV.dto.request.BookUpdateRequest;
import com.example.QLTV.dto.response.BookResponse;
import com.example.QLTV.enity.Book;
import com.example.QLTV.enity.BookCopy;
import com.example.QLTV.enity.enums.BookCondition;
import com.example.QLTV.enity.enums.BookStatus;
import com.example.QLTV.exception.ApiException;
import com.example.QLTV.exception.ErrorCode;
import com.example.QLTV.mapper.IBookMapper;
import com.example.QLTV.repository.IBookCopyRepository;
import com.example.QLTV.repository.IBookRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookServiceImpl implements IBookService {

    IBookRepository bookRepository;
    IBookCopyRepository bookCopyRepository;
    IBookMapper bookMapper;

    @Override
    @Transactional
    public BookResponse createBook(BookCreationRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ApiException(ErrorCode.ISBN_EXISTED);
        }

        Book book = bookMapper.toBook(request);
        book.setIsDeleted(false);
        book.setCreatedAt(LocalDateTime.now());
        book = bookRepository.save(book);

        // Tự động tạo bản in (BookCopy) theo số lượng quantity
        createCopiesForBook(book, request.getQuantity());

        return bookMapper.toBookResponse(book);
    }

    @Override
    public List<BookResponse> findAllBooks() {
        // Lọc các đầu sách chưa bị xóa mềm
        return bookRepository.findAll().stream()
                .filter(book -> !Boolean.TRUE.equals(book.getIsDeleted()))
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse getBookById(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));
        return bookMapper.toBookResponse(book);
    }

    @Override
    @Transactional
    public BookResponse updateBook(UUID bookId, BookUpdateRequest request) {
        Book book = bookRepository.findById(bookId)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));

        bookMapper.updateBook(book, request);
        book.setUpdatedAt(LocalDateTime.now());
        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponse updateShelf(UUID bookId, String shelfCode) {
        Book book = bookRepository.findById(bookId)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));

        book.setShelfCode(shelfCode);
        book.setUpdatedAt(LocalDateTime.now());
        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponse updateStock(UUID bookId, int additionalQuantity) {
        Book book = bookRepository.findById(bookId)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));

        if (additionalQuantity > 0) {
            createCopiesForBook(book, additionalQuantity);
        } else if (additionalQuantity < 0) {
            // Lấy danh sách bản sao sẵn có và chưa bị xóa
            List<BookCopy> availableCopies = bookCopyRepository.findAllByBookIdAndIsDeletedFalse(bookId).stream()
                    .filter(copy -> copy.getCirculationStatus() == BookStatus.AVAILABLE)
                    .collect(Collectors.toList());

            int removeCount = Math.abs(additionalQuantity);
            if (availableCopies.size() < removeCount) {
                throw new ApiException(ErrorCode.INVALID_PARAM);
            }

            // Thực hiện xóa mềm cho các bản sao
            for (int i = 0; i < removeCount; i++) {
                BookCopy copy = availableCopies.get(i);
                copy.setIsDeleted(true);
                copy.setUpdatedAt(LocalDateTime.now());
            }
            bookCopyRepository.saveAll(availableCopies.subList(0, removeCount));
        }

        return bookMapper.toBookResponse(book);
    }

    @Override
    @Transactional
    public void deleteBook(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));

        // Kiểm tra xem có bản sao nào đang được mượn không
        List<BookCopy> activeCopies = bookCopyRepository.findAllByBookIdAndIsDeletedFalse(bookId);
        boolean isAnyBorrowed = activeCopies.stream()
                .anyMatch(c -> c.getCirculationStatus() == BookStatus.BORROWED);

        if (isAnyBorrowed) {
            throw new ApiException(ErrorCode.COPY_NOT_AVAILABLE);
        }

        // Xóa mềm đầu sách
        book.setIsDeleted(true);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);

        // Xóa mềm toàn bộ các bản sao liên quan
        activeCopies.forEach(copy -> {
            copy.setIsDeleted(true);
            copy.setUpdatedAt(LocalDateTime.now());
        });
        bookCopyRepository.saveAll(activeCopies);
    }

    private void createCopiesForBook(Book book, int quantity) {
        List<BookCopy> copies = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            BookCopy copy = new BookCopy();
            copy.setBook(book);
            copy.setBarcode(book.getIsbn() + "-" + System.nanoTime() + "-" + i);
            copy.setCirculationStatus(BookStatus.AVAILABLE);
            copy.setConditionStatus(BookCondition.NEW);
            copy.setIsDeleted(false);
            copy.setCreatedAt(LocalDateTime.now());
            copies.add(copy);
        }
        bookCopyRepository.saveAll(copies);
    }
}