package com.freeboard01.domain.user;

import com.freeboard01.api.user.UserForm;
import com.freeboard01.domain.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean join(UserForm user) {
        UserEntity userEntity = userRepository.findByAccountId(user.getAccountId());
        if (userEntity == null) {
            UserEntity newUser = user.convertUserEntity();
            newUser.setRole(UserRole.NORMAL);
            userRepository.save(newUser);
            return true;
        }
        return false;
    }

    public Boolean login(UserForm user) {
        UserEntity userEntity = userRepository.findByAccountId(user.getAccountId());
        return userEntity != null && userEntity.getPassword().equals(user.getPassword());
    }

}
