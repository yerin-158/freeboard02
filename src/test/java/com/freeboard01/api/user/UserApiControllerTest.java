package com.freeboard01.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeboard01.domain.user.UserEntity;
import com.freeboard01.domain.user.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
@Transactional
@WebAppConfiguration
@Rollback(value = false)
public class UserApiControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mvc;

    @BeforeEach
    public void initMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private String randomId(){
        String id = "";
        for(int i = 0; i < 10; i++) {
            double dValue = Math.random();
            if(i%2 == 0) {
                id += (char) ((dValue * 26) + 65);   // 대문자
                continue;
            }
            id += (char)((dValue * 26) + 97); // 소문자
        }
        return id;
    }

    @Test
    @DisplayName("동일한 아이디를 가진 유저가 없으면 가입에 성공한다.")
    public void joinTest1() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        UserForm userForm = UserForm.builder().accountId(randomId()).password("password").build();
        mvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(userForm))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("동일한 아이디를 가진 유저가 있으면 가입에 실패한다.")
    public void joinTest2() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserEntity userEntity = userRepository.findAll().get(0);
        UserForm userForm = UserForm.builder().accountId(userEntity.getAccountId()).password("password").build();
        mvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(userForm))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("비밀번호를 올바르게 입력하면 로그인되고, 세션에 저장된다.")
    public void loginTest1() throws Exception {

    }
}
