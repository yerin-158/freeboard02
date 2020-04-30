package com.freeboard01.domain.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
public class boardRepositoryIntegrationTest {

    @Autowired
    private BoardRepository boardRepository = null;

    @Test
    public void test1() {
        List<BoardEntity> boardEntityList = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            BoardEntity entity = BoardEntity.builder().user("myNameis" + i).title("제목입니다." + i + i + i).password("1234").contents("test data~" + i).build();
            boardEntityList.add(entity);
        }
        boardRepository.saveAll(boardEntityList);
    }

}
