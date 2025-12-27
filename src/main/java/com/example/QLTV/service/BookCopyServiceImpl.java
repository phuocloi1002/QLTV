package com.example.QLTV.service;

import com.example.QLTV.dto.request.BookCopyStatusRequest;
import com.example.QLTV.dto.response.BookCopyResponse;
import com.example.QLTV.enity.BookCopy;
import com.example.QLTV.enity.enums.BookCondition;
import com.example.QLTV.enity.enums.BookStatus;
import com.example.QLTV.exception.ApiException;
import com.example.QLTV.exception.ErrorCode;
import com.example.QLTV.mapper.IBookCopyMapper;
import com.example.QLTV.repository.IBookCopyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookCopyServiceImpl implements IBookCopyService {

    IBookCopyRepository bookCopyRepository;
    IBookCopyMapper bookCopyMapper;

    @Override
    @Transactional
    public BookCopyResponse updateStatus(UUID copyId, BookCopyStatusRequest request) {
        // 1. Tìm bản in theo ID
        BookCopy copy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ApiException(ErrorCode.COPY_NOT_FOUND));

        // 2. Cập nhật trạng thái lưu thông (Mượn/Trả/Mất)
        if (request.getCirculationStatus() != null) {
            copy.setCirculationStatus(request.getCirculationStatus());
        }

        // 3. Cập nhật tình trạng vật lý (Mới/Hỏng)
        if (request.getConditionStatus() != null) {
            copy.setConditionStatus(request.getConditionStatus());

            // Logic nghiệp vụ: Nếu sách hỏng (DAMAGED), tự động chuyển trạng thái lưu thông thành DAMAGED
            if (request.getConditionStatus() == BookCondition.DAMAGED) {
                copy.setCirculationStatus(BookStatus.DAMAGED);
            }
        }

        // 4. Lưu và trả về kết quả qua Mapper
        return bookCopyMapper.toBookCopyResponse(bookCopyRepository.save(copy));
    }

    @Override
    public BookCopyResponse getCopyById(UUID copyId) {
        return bookCopyRepository.findById(copyId)
                .map(bookCopyMapper::toBookCopyResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.COPY_NOT_FOUND));
    }

    @Override
    public List<BookCopyResponse> getCopiesByBookId(UUID bookId) {
        return bookCopyRepository.findAllByBookId(bookId).stream()
                .map(bookCopyMapper::toBookCopyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCopy(UUID copyId) {
        BookCopy copy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ApiException(ErrorCode.COPY_NOT_FOUND));

        // Kiểm tra nếu sách đang được mượn thì không cho xóa
        if (copy.getCirculationStatus() == BookStatus.BORROWED) {
            throw new ApiException(ErrorCode.COPY_CANNOT_DELETE_BORROWED);
        }

        bookCopyRepository.delete(copy);
    }
}