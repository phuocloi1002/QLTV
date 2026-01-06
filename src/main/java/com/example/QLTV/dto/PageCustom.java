package com.example.QLTV.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PageCustom {
    int totalPages;
    int totalElements;
    int number;//trang dang dung tinh tu 0
    int size;//so luong record tren 1 trang

    public  PageCustom(Page page)
    {
        totalPages=page.getTotalPages();
        number=page.getNumber();
        size=page.getSize();
        totalElements=page.getNumberOfElements();
    }
}
