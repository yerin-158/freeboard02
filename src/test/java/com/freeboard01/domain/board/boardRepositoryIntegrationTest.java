package com.freeboard01.domain.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
public class boardRepositoryIntegrationTest {

    @Autowired
    private BoardRepository boardRepository = null;

    @Test
    public void test1(){
        BoardEntity entity = BoardEntity.builder().user("myName").password("1234").contents("test data~").build();
        boardRepository.save(entity);
    }

}
