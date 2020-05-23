package com.freeboard02.domain.board;

import com.freeboard02.domain.user.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    int findTotalSize();

    List<BoardEntity> findAllBoardEntity();

    Optional<BoardEntity> findById(long id);

    void save(BoardEntity boardEntity);

    void updateById(BoardEntity boardEntity);

    void deleteById(long id);

    List<BoardEntity> findAllByWriterId(long writerId);

    List<BoardEntity> findAllByWriterIn(@Param("userEntityList") List<UserEntity> userEntityList, @Param("start") int start, @Param("pageSize") int pageSize);

    List<BoardEntity> findAll(@Param("searchType") String searchType, @Param("target") String target, @Param("start") int start, @Param("pageSize") int pageSize);

    List<BoardEntity> findAllWithPaging(@Param("start") int start, @Param("pageSize") int pageSize);
}
