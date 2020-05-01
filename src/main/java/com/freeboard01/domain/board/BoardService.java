package com.freeboard01.domain.board;

import com.freeboard01.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class BoardService {

    private BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    public Page<BoardEntity> get(Pageable pageable) {
        return boardRepository.findAll(PageUtil.convertToZeroBasePageWithSort(pageable));
    }

    public BoardEntity post(BoardEntity entity){
        return boardRepository.save(entity);
    }

    public BoardEntity update(BoardEntity newBoard, long id) {
        BoardEntity prevEntity = boardRepository.findById(id).get();
        if(prevEntity.getPassword().equals(newBoard.getPassword())){
            boardRepository.deleteById(id);
            return prevEntity.update(newBoard);
        }
        return null;
    }

    public boolean delete(long id, String password) {
        BoardEntity entity = boardRepository.findById(id).get();
        if(entity.getPassword().equals(password)){
            boardRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
