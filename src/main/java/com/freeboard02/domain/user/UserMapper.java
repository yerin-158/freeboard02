package com.freeboard02.domain.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void save(UserEntity userEntity);

    UserEntity findByAccountId(String accountId);

    List<UserEntity> findByAccountIdLike(String target);

    List<UserEntity> findAll();
}
