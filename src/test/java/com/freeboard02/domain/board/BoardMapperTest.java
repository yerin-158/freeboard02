package com.freeboard02.domain.board;

import com.freeboard02.api.board.BoardForm;
import com.freeboard02.domain.board.enums.SearchType;
import com.freeboard02.domain.user.UserEntity;
import com.freeboard02.domain.user.UserRepository;
import com.github.npathai.hamcrestopt.OptionalMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
@Rollback(value = false)
public class BoardMapperTest {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    private BoardEntity targetBoard;

    final int PAGE = 0;
    final int SIZE = 10;

    @BeforeEach
    public void init() {
        user = userRepository.findAll().get(3);
        targetBoard = BoardEntity.builder()
                .title(randomString())
                .contents(randomString())
                .writer(user).build();
    }


    @Test
    public void mapperFind() {
        List<BoardEntity> boardEntityList = boardMapper.findAllBoardEntity();
        List<BoardEntity> boardEntityList2 = boardRepository.findAll();

        assertThat(boardEntityList.size(), equalTo(boardEntityList2.size()));
    }

    @Test
    public void mapperInsert() {
        assertThat(targetBoard.getId(), equalTo(0L));
        boardMapper.save(targetBoard);
        assertThat(targetBoard.getId(), not(equalTo(0L)));
    }

    @Test
    public void mapperFindById() {
        boardMapper.save(targetBoard);

        BoardEntity entity = boardMapper.findById(targetBoard.getId()).get();

        assertThat(targetBoard.getTitle(), equalTo(entity.getTitle()));
        assertThat(targetBoard.getContents(), equalTo(entity.getContents()));
    }

    @Test
    public void mapperDelete() {
        boardMapper.save(targetBoard);

        long targetId = targetBoard.getId();
        assertThat(targetId, not(equalTo(0L)));

        Optional<BoardEntity> saved = boardMapper.findById(targetId);
        assertThat(saved, OptionalMatchers.isPresent());

        boardMapper.deleteById(targetId);
        Optional<BoardEntity> deleted = boardMapper.findById(targetId);
        assertThat(deleted, OptionalMatchers.isEmpty());
    }

    @Test
    public void mapperUpdate() {
        boardMapper.save(targetBoard);

        BoardForm form = BoardForm.builder().contents(randomString()).title(randomString()).build();
        BoardEntity entity = form.convertBoardEntity(targetBoard.getWriter());
        entity.setId(targetBoard.getId());
        boardMapper.updateById(entity);

        BoardEntity updatedEntity = boardMapper.findById(targetBoard.getId()).get();
        assertThat(updatedEntity.getContents(), equalTo(form.getContents()));
        assertThat(updatedEntity.getTitle(), equalTo(form.getTitle()));
    }

    @Test
    public void mapperPaging() {
        String time = LocalDateTime.now().toString();
        List<Long> savedEntityIds = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            BoardEntity boardEntity = BoardEntity.builder().writer(user).contents(time).title("title").build();
            boardMapper.save(boardEntity);
            savedEntityIds.add(boardEntity.getId());
        }

        List<BoardEntity> findEntities = boardMapper.findAll(SearchType.CONTENTS.name(), time, PAGE, SIZE);
        List<Long> findEntityIds = findEntities.stream().map(boardEntity -> boardEntity.getId()).collect(Collectors.toList());
        assertThat(findEntities.size(), equalTo(SIZE));
        assertThat(savedEntityIds, hasItems(findEntityIds.toArray(new Long[SIZE])));
    }

    private String randomString() {
        String id = "";
        for (int i = 0; i < 20; i++) {
            double dValue = Math.random();
            if (i % 2 == 0) {
                id += (char) ((dValue * 26) + 65);   // 대문자
                continue;
            }
            id += (char) ((dValue * 26) + 97); // 소문자
        }
        return id;
    }
}
