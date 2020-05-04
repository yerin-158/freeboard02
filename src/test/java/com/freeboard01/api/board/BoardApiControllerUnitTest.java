package com.freeboard01.api.board;


import com.freeboard01.api.user.UserForm;
import com.freeboard01.domain.board.BoardEntity;
import com.freeboard01.domain.board.BoardService;
import com.freeboard01.domain.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
@Transactional
public class BoardApiControllerUnitTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardApiController sut;

    /*@Test
    public void postTest1(){
        UserForm userForm = UserForm.builder().accountId("yerin").password("pass123").build();
        UserEntity userEntity = userForm.convertUserEntity();
        BoardForm boardForm = BoardForm.builder().title("session test").build();
        BoardEntity boardEntity = boardForm.convertBoardEntity(userEntity);

        HttpSession session = mock(HttpSession.class);

        given(session.getAttribute("USER")).willReturn(userForm);
        given(boardService.post(boardForm, userForm)).willReturn(boardEntity);

        sut.post(boardForm);
    }*/
}
