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
    public List<BookResponse> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookResponse createBook(BookCreationRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ApiException(ErrorCode.ISBN_EXISTED);
        }

        Book book = bookMapper.toBook(request);
        book = bookRepository.save(book);

        // Logic 3.2: Tự động tạo bản in (BookCopy) theo số lượng quantity
        createCopiesForBook(book, request.getQuantity());

        return bookMapper.toBookResponse(book);
    }

    @Override
    public BookResponse getBookById(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));
        return bookMapper.toBookResponse(book);
    }

    @Override
    @Transactional
    public BookResponse updateBook(UUID bookId, BookUpdateRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));

        bookMapper.updateBook(book, request);
        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponse updateShelf(UUID bookId, String shelfCode) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));

        book.setShelfCode(shelfCode);
        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponse updateStock(UUID bookId, int additionalQuantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));

        if (additionalQuantity > 0) {
            createCopiesForBook(book, additionalQuantity);
        } else if (additionalQuantity < 0) {
            // Logic giảm số lượng: Tìm những bản in đang AVAILABLE để xóa
            List<BookCopy> availableCopies = bookCopyRepository.findAllByBookId(bookId).stream()
                    .filter(c -> c.getCirculationStatus() == BookStatus.AVAILABLE)
                    .limit(Math.abs(additionalQuantity))
                    .collect(Collectors.toList());

            if (availableCopies.size() < Math.abs(additionalQuantity)) {
                throw new ApiException(ErrorCode.INVALID_PARAM); // Hoặc mã lỗi "Không đủ sách sẵn có để giảm"
            }
            bookCopyRepository.deleteAll(availableCopies);
        }

        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteBook(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOK_NOT_FOUND));

        boolean isAnyBorrowed = book.getCopies().stream()
                .anyMatch(c -> c.getCirculationStatus() == BookStatus.BORROWED);

        if (isAnyBorrowed) {
            throw new ApiException(ErrorCode.COPY_CANNOT_DELETE_BORROWED);
        }

        bookRepository.delete(book);
    }

    // Hàm hỗ trợ tạo bản in với Barcode duy nhất
    private void createCopiesForBook(Book book, int quantity) {
        List<BookCopy> copies = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            BookCopy copy = new BookCopy();
            copy.setBook(book);
            // Barcode định dạng: ISBN + NanoTime để đảm bảo không trùng
            copy.setBarcode(book.getIsbn() + "-" + System.nanoTime() + "-" + i);
            copy.setCirculationStatus(BookStatus.AVAILABLE);
            copy.setConditionStatus(BookCondition.NEW);
            copies.add(copy);
        }
        bookCopyRepository.saveAll(copies);
    }
}