package com.freeboard01.api.board;

import com.freeboard01.api.PageDto;
import com.freeboard01.domain.board.BoardEntity;
import com.freeboard01.domain.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<PageDto<BoardDto>> get(@PageableDefault(page = 1, size = 10, sort = "createdAt", direction = Sort.Direction.DESC )Pageable pageable){
        Page<BoardEntity> pageBoardList = boardService.get(pageable);
        List<BoardDto> boardDtoList = pageBoardList.stream().map(boardEntity -> BoardDto.of(boardEntity)).collect(Collectors.toList());
        return ResponseEntity.ok(PageDto.of(pageBoardList, boardDtoList));
    }

    @PostMapping
    public ResponseEntity<BoardDto> post(@RequestBody BoardForm form){
        BoardEntity savedEntity = boardService.post(form.convertBoardEntity());
        return ResponseEntity.ok(BoardDto.of(savedEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@RequestBody BoardForm form, @PathVariable long id){
        return ResponseEntity.ok(boardService.update(form.convertBoardEntity(), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable long id, @RequestParam String password){
        return ResponseEntity.ok(boardService.delete(id, password));
    }
}
