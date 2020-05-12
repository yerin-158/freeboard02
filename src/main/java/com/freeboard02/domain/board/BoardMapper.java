package com.freeboard02.domain.board;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardEntity> findAllBoardEntity();
}
