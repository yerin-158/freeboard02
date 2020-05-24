package com.freeboard02.api.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeboard02.api.user.UserForm;
import com.freeboard02.domain.board.BoardEntity;
import com.freeboard02.domain.board.BoardMapper;
import com.freeboard02.domain.board.enums.BoardExceptionType;
import com.freeboard02.domain.board.enums.SearchType;
import com.freeboard02.domain.user.UserEntity;
import com.freeboard02.domain.user.UserMapper;
import com.freeboard02.util.exception.FreeBoardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
@Transactional
@WebAppConfiguration
public class BoardApiControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private UserMapper userMapper;

    private MockMvc mvc;

    @Autowired
    private MockHttpSession mockHttpSession;

    private UserEntity testUser;
    private BoardEntity testBoard;


    @BeforeEach
    public void initMvc() {
        testUser = userMapper.findAll().get(0);
        UserForm userForm = UserForm.builder().accountId(testUser.getAccountId()).password(testUser.getPassword()).build();
        mockHttpSession.setAttribute("USER", userForm);

        testBoard = boardMapper.findAllByWriterId(testUser.getId()).get(0);

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getTest() throws Exception {
        mvc.perform(get("/api/boards")).andExpect(status().isOk());
    }

    @Test
    public void saveTest() throws Exception {
        BoardForm boardForm = BoardForm.builder().title("제목을 입력하세요").contents("내용입니다.").build();
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(post("/api/boards")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(boardForm)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("본인이 작성한 글은 데이터 수정이 가능하다.")
    public void updateTest() throws Exception {
        BoardForm updateForm = BoardForm.builder().title("제목을 입력하세요").contents("수정된 데이터입니다 ^^*").build();
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(put("/api/boards/"+testBoard.getId())
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateForm)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("남이 작성한 글은 수정이 불가하다.")
    public void updateTest2() throws Exception {
        BoardEntity wrongBoard = new BoardEntity();

        List<BoardEntity> boardEntities = boardMapper.findAllBoardEntity();
        for(int i =0; i<boardEntities.size(); ++i){
            if(boardEntities.get(i).getWriter().getId() != testUser.getId()){
                wrongBoard = boardEntities.get(i);
                break;
            }
        }

        BoardForm updateForm = BoardForm.builder().title("제목을 입력하세요").contents("수정된 데이터입니다 ^^*").build();
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(put("/api/boards/"+wrongBoard.getId())
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateForm)))
                .andExpect(result -> assertEquals(result.getResolvedException().getClass().getCanonicalName(), FreeBoardException.class.getCanonicalName()))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), BoardExceptionType.NO_QUALIFICATION_USER.getErrorMessage()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("자신이 작성한 글이 아닌데 삭제를 시도할 경우 Exception 처리된다.")
    public void deleteTest1() throws Exception {

        BoardEntity wrongBoard = new BoardEntity();

        List<BoardEntity> boardEntities = boardMapper.findAllBoardEntity();
        for(int i =0; i<boardEntities.size(); ++i){
            if(boardEntities.get(i).getWriter().getId() != testUser.getId()){
                wrongBoard = boardEntities.get(i);
                break;
            }
        }

        mvc.perform(delete("/api/boards/"+wrongBoard.getId())
                .session(mockHttpSession))
                .andExpect(result -> assertEquals(result.getResolvedException().getClass().getCanonicalName(), FreeBoardException.class.getCanonicalName()))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), BoardExceptionType.NO_QUALIFICATION_USER.getErrorMessage()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("권한있는 유저가 데이터 삭제를 시도하면 수행된다.")
    public void deleteTest2() throws Exception {
        mvc.perform(delete("/api/boards/"+testBoard.getId())
                .session(mockHttpSession))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시판 검색 테스트-타이틀")
    public void searchTest() throws Exception {
        String keyword = "test";
        mvc.perform(get("/api/boards?type="+SearchType.TITLE+"&keyword="+keyword)
                .session(mockHttpSession))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시판 검색 테스트-글 작성자")
    public void searchTest2() throws Exception {
        String keyword = "yerin";
        mvc.perform(get("/api/boards?type="+SearchType.WRITER+"&keyword="+keyword)
                .session(mockHttpSession))
                .andExpect(status().isOk());
    }

}
