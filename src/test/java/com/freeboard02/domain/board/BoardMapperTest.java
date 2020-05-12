package com.freeboard02.domain.board;

import com.freeboard02.api.board.BoardForm;
import com.freeboard02.domain.user.UserEntity;
import com.freeboard02.domain.user.UserRepository;
import com.github.npathai.hamcrestopt.OptionalMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

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
