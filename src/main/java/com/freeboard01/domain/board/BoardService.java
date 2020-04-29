package com.freeboard01.domain.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BoardService {

    public List<BoardEntity> get() {
        List<BoardEntity> boardEntityList = new ArrayList<>();
        for (int i = 1; i <= 5; ++i) {
            BoardEntity boardEntity = BoardEntity.builder().user("user"+i+i+i).title("title"+i+i+i).contents("contents"+i+i+i).password("1234").build();
            boardEntityList.add(boardEntity);
        }
        return boardEntityList;
    }
}
