package com.example.QLTV.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {
    List<T> content;
    PageCustom pageable;
    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageable = new PageCustom(page);
    }
}
