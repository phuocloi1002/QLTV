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

import java.time.LocalDateTime;
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
        // 1. Tìm bản in theo ID và đảm bảo chưa bị xóa mềm
        BookCopy copy = bookCopyRepository.findById(copyId)
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
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

        copy.setUpdatedAt(LocalDateTime.now());

        // 4. Lưu và trả về kết quả qua Mapper
        return bookCopyMapper.toBookCopyResponse(bookCopyRepository.save(copy));
    }

    @Override
    public BookCopyResponse getCopyById(UUID copyId) {
        return bookCopyRepository.findById(copyId)
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted())) // Chỉ lấy bản chưa xóa
                .map(bookCopyMapper::toBookCopyResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.COPY_NOT_FOUND));
    }

    @Override
    public List<BookCopyResponse> getCopiesByBookId(UUID bookId) {
        // Sử dụng phương thức đã thêm vào Repository để lọc IsDeleted = False
        return bookCopyRepository.findAllByBookIdAndIsDeletedFalse(bookId).stream()
                .map(bookCopyMapper::toBookCopyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCopy(UUID copyId) {
        // 1. Tìm bản sao trong DB
        BookCopy copy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ApiException(ErrorCode.COPY_NOT_FOUND));

        // 2. Kiểm tra nếu đã bị xóa mềm trước đó
        if (Boolean.TRUE.equals(copy.getIsDeleted())) {
            throw new ApiException(ErrorCode.COPY_NOT_FOUND);
        }

        // 3. Kiểm tra ràng buộc: Không được xóa nếu sách đang bị mượn [cite: 97]
        if (copy.getCirculationStatus() == BookStatus.BORROWED) {
            throw new ApiException(ErrorCode.COPY_NOT_AVAILABLE);
        }

        // 4. Thực hiện xóa mềm (Cập nhật field từ BaseEntity)
        copy.setIsDeleted(true);
        copy.setUpdatedAt(LocalDateTime.now());

        // 5. Lưu lại trạng thái mới thay vì xóa vật lý
        bookCopyRepository.save(copy);
    }
}