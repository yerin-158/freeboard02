package com.freeboard01.domain.board;

import com.freeboard01.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<BoardEntity> get(Pageable pageable) {
        return boardRepository.findAll(PageUtil.convertToZeroBasePageWithSort(pageable));
    }

    public BoardEntity post(BoardEntity entity){
        return boardRepository.save(entity);
    }

    public BoardEntity update(BoardEntity newBoard, long id) {
        BoardEntity prevEntity = boardRepository.findById(id).get();
        return prevEntity.update(newBoard);
    }
}
