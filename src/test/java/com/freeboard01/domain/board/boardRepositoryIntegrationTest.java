package com.freeboard01.domain.board;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
@Rollback(value = false)
public class boardRepositoryIntegrationTest {

    @Autowired
    private BoardRepository boardRepository = null;

    @Test
    public void test1(){
        BoardEntity entity = BoardEntity.builder().user("myName").password("1234").content("test data~").build();
        boardRepository.save(entity);
    }

}
