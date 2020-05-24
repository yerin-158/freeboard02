package com.freeboard02.api.board;

import com.freeboard02.api.PageDto;
import com.freeboard02.api.user.UserForm;
import com.freeboard02.domain.board.BoardEntity;
import com.freeboard02.domain.board.BoardService;
import com.freeboard02.domain.board.enums.SearchType;
import com.freeboard02.domain.user.enums.UserExceptionType;
import com.freeboard02.util.PageUtil;
import com.freeboard02.util.exception.FreeBoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final HttpSession httpSession;
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<PageDto<BoardDto>> get(@PageableDefault(page = 1, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<BoardEntity> pageBoardList = boardService.get(PageUtil.convertToZeroBasePageWithSort(pageable));
        List<BoardDto> boardDtoList = pageBoardList.stream().map(boardEntity -> BoardDto.of(boardEntity)).collect(Collectors.toList());
        return ResponseEntity.ok(PageDto.of(boardService.getTotalSize(), PageUtil.convertToZeroBasePageWithSort(pageable), boardDtoList));
    }

    @PostMapping
    public void post(@RequestBody BoardForm form) {
        if (httpSession.getAttribute("USER") == null) {
            throw new FreeBoardException(UserExceptionType.LOGIN_INFORMATION_NOT_FOUND);
        }
        boardService.post(form, (UserForm) httpSession.getAttribute("USER"));
    }

    @PutMapping("/{id}")
    public void update(@RequestBody BoardForm form, @PathVariable long id) {
        if (httpSession.getAttribute("USER") == null) {
            throw new FreeBoardException(UserExceptionType.LOGIN_INFORMATION_NOT_FOUND);
        }
        boardService.update(form, (UserForm) httpSession.getAttribute("USER"), id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        if (httpSession.getAttribute("USER") == null) {
            throw new FreeBoardException(UserExceptionType.LOGIN_INFORMATION_NOT_FOUND);
        }
        boardService.delete(id, (UserForm) httpSession.getAttribute("USER"));
    }

    @GetMapping(params = {"type", "keyword"})
    public ResponseEntity<PageDto<BoardDto>> search(@PageableDefault(page = 1, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                    @RequestParam String keyword, @RequestParam SearchType type) {
        if (httpSession.getAttribute("USER") == null) {
            throw new FreeBoardException(UserExceptionType.LOGIN_INFORMATION_NOT_FOUND);
        }
        return ResponseEntity.ok(boardService.search(PageUtil.convertToZeroBasePageWithSort(pageable), keyword, type));
    }
}
