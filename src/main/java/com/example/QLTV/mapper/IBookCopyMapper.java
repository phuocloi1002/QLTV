package com.example.QLTV.mapper;

import com.example.QLTV.dto.response.BookCopyResponse;
import com.example.QLTV.enity.BookCopy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IBookCopyMapper {

    @Mapping(target = "bookTitle", source = "book.title")
    BookCopyResponse toBookCopyResponse(BookCopy bookCopy);
}