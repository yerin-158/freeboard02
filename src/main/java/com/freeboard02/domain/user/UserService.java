package com.freeboard02.domain.user;

import com.freeboard02.api.user.UserForm;
import com.freeboard02.domain.user.enums.UserExceptionType;
import com.freeboard02.domain.user.enums.UserRole;
import com.freeboard02.util.exception.FreeBoardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserRole findUserRole(UserForm user){
        return userMapper.findByAccountId(user.getAccountId()).getRole();
    }

    public void join(UserForm user) {
        if (userMapper.findByAccountId(user.getAccountId()) != null){
            throw new FreeBoardException(UserExceptionType.DUPLICATED_USER);
        }
        UserEntity newUser = user.convertUserEntity();
        newUser.setRole(UserRole.NORMAL);
        userMapper.save(newUser);
    }

    public void login(UserForm user) {
        UserEntity userEntity = userMapper.findByAccountId(user.getAccountId());
        if (userEntity == null){
            throw new FreeBoardException(UserExceptionType.NOT_FOUND_USER);
        }
        if (userEntity.getPassword().equals(user.getPassword()) == false){
            throw new FreeBoardException(UserExceptionType.WRONG_PASSWORD);
        }
    }

}
