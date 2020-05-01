package com.freeboard01.api.board;


import com.freeboard01.api.PageDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
@Transactional
public class BoardApiControllerUnitTest {

    @Autowired
    private BoardApiController sut;

    @Test
    public void getTest(){
        PageDto<BoardDto> list = sut.get(PageRequest.of(0,10, Sort.by(Sort.Direction.DESC, "createdAt"))).getBody();
        assertThat(list.getContents().size(), greaterThanOrEqualTo(1));
    }
}
