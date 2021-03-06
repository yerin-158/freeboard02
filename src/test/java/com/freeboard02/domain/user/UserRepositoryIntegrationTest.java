package com.freeboard02.domain.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
@Rollback(value = false)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserMapper userMapper;

    @Disabled
    @Test
    @DisplayName("유저를 추가한다.")
    public void test1() {
        UserEntity user = UserEntity.builder().accountId("testUser").password("pass123").build();
        userMapper.save(user);
    }

}
