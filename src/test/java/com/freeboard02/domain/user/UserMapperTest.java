package com.freeboard02.domain.user;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void saveTest() {
        String accountId = randomString();
        UserEntity userEntity = UserEntity.builder()
                .accountId(accountId)
                .password(randomString())
                .build();
        assertThat(userEntity.getId(), equalTo(0L));
        userMapper.save(userEntity);
        assertThat(userEntity.getId(), not(equalTo(0L)));
    }

    @Test
    public void findOneTest() {
        String accountId = randomString();
        UserEntity userEntity = UserEntity.builder()
                .accountId(accountId)
                .password(randomString())
                .build();
        userMapper.save(userEntity);

        UserEntity findEntity = userMapper.findByAccountId(accountId);
        assertThat(userEntity.getId(), equalTo(findEntity.getId()));
    }

    @Test
    public void findAllLikeTest() {
        String time = LocalDateTime.now().toString();
        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            UserEntity userEntity = UserEntity.builder()
                    .accountId(randomString() + time)
                    .password(randomString())
                    .build();
            userMapper.save(userEntity);
            userEntities.add(userEntity);
        }

        List<UserEntity> findEntities = userMapper.findByAccountIdLike(time);

        List<Long> saveEntityIds = userEntities.stream().map(userEntity -> userEntity.getId()).collect(Collectors.toList());
        List<Long> findEntityIds = findEntities.stream().map(userEntity -> userEntity.getId()).collect(Collectors.toList());

        assertEquals(saveEntityIds, findEntityIds);
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
