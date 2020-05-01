package com.freeboard01.api.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeboard01.domain.board.BoardEntity;
import com.freeboard01.domain.board.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
@Transactional
@WebAppConfiguration
@Rollback(value = false)
public class BoardApiControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BoardRepository boardRepository;

    private MockMvc mvc;

    @BeforeEach
    public void initMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("trailing-slash test")
    public void trailingSlashTest() throws Exception {
        mvc.perform(get("/api/boards/")).andExpect(status().isOk());
    }

    @Test
    public void getTest() throws Exception {
        mvc.perform(get("/api/boards")).andExpect(status().isOk());
    }

    @Test
    public void saveTest() throws Exception {
        BoardForm boardForm = BoardForm.builder().user("사용자 아이디").title("제목을 입력하세요").contents("내용입니다.").password("1234").build();
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(boardForm)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'contents':'"+boardForm.getContents()+"'}"));;
    }

    @Test
    @DisplayName("올바른 패스워드를 입력한 경우 데이터 수정이 가능하다.")
    public void updateTest() throws Exception {
        BoardEntity entity = boardRepository.findAll().get(0);
        BoardForm updateForm = BoardForm.builder().user("사용자 아이디").title("제목을 입력하세요").contents("수정된 데이터입니다 ^^*").password(entity.getPassword()).build();
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(put("/api/boards/"+entity.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateForm)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'contents':'"+updateForm.getContents()+"'}"));
    }

    @Test
    @DisplayName("잘못된 패스워드를 입력한 경우 데이터는 삭제되지 않고 false를 반환한다.")
    public void deleteTest1() throws Exception {
        BoardEntity entity = boardRepository.findAll().get(0);

        mvc.perform(delete("/api/boards/"+entity.getId()+"?password=wrongPass"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("올바른 패스워드를 입력한 경우 데이터를 삭제하고 true를 반환한다.")
    public void deleteTest2() throws Exception {
        BoardEntity entity = boardRepository.findAll().get(0);

        mvc.perform(delete("/api/boards/"+entity.getId()+"?password="+entity.getPassword()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }


}
