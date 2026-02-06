package com.example.QLTV.mapper;

import com.example.QLTV.dto.response.FineResponse;
import com.example.QLTV.enity.Fine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IFineMapper {
    @Mapping(target = "bookTitle",source = "loan.bookCopy.book.title")
    FineResponse toFineResponse(Fine fine);

    List<FineResponse> toFineResponseList(List<Fine> fines);

}
