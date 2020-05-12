package com.freeboard02.domain.board;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    List<BoardEntity> findAllBoardEntity();

    Optional<BoardEntity> findById(long id);

    void save(BoardEntity boardEntity);

    void updateById(BoardEntity boardEntity);

    void deleteById(long id);
}
