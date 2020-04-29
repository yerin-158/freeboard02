package com.freeboard01.api.board;

import com.freeboard01.domain.board.BoardEntity;
import com.freeboard01.domain.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
public class BoardApiController {
    private final BoardService boardService;

    @Autowired
    public BoardApiController(BoardService boardService){
        this.boardService = boardService;
    }

    @GetMapping
    public ResponseEntity<List<BoardDto>> get(){
        List<BoardEntity> boardEntityList = boardService.get();
        return ResponseEntity.ok(boardEntityList.stream().map(boardEntity -> BoardDto.of(boardEntity)).collect(Collectors.toList()));
    }
}
