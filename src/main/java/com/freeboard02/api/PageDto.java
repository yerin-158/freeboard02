package com.freeboard02.api;

import com.freeboard02.api.board.BoardDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@NoArgsConstructor
@Getter
public class PageDto<T> {

    private final static int VIEWPAGESIZE = 10;
    private int startPage;
    private int endPage;
    private int totalPages;
    private List<T> contents;

    private PageDto(int startPage, int endPage, int totalPages, List<T> contents) {
        this.startPage = startPage;
        this.endPage = endPage;
        this.totalPages = totalPages;
        this.contents = contents;
    }

    public static <T> PageDto of(int totalDataSize, Pageable pageable, List<T> contents) {
        int totalPages = (int) Math.ceil(((long) totalDataSize) / pageable.getPageSize());
        int nowPage = pageable.getPageNumber() + 1;
        int startPage = 1 * VIEWPAGESIZE * (nowPage / VIEWPAGESIZE);
        int endPage = startPage + VIEWPAGESIZE - 1;

        return new PageDto<>(startPage == 0 ? 1 : startPage, endPage > totalPages ? totalPages : endPage, totalPages, contents);
    }
}
