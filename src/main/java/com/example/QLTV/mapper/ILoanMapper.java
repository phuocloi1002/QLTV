package com.example.QLTV.mapper;

import com.example.QLTV.dto.response.BorrowResponse;
import com.example.QLTV.enity.Fine;
import com.example.QLTV.enity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ILoanMapper {
    @Mapping(target = "bookTitle", source = "bookCopy.book.title")
    @Mapping(target = "barcode", source = "bookCopy.barcode")
    @Mapping(target = "borrowDate", source = "borrowedAt")
    @Mapping(target = "fineAmount", source = "fines", qualifiedByName = "calculateTotalFine")
    BorrowResponse toBorrowResponse(Loan loan);

    @Named("calculateTotalFine")
    default Double calculateTotalFine(List<Fine> fines) {
        if (fines == null || fines.isEmpty()) return 0.0;
        return fines.stream().mapToDouble(Fine::getAmount).sum();
    }

}
