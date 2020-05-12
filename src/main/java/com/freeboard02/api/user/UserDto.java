package com.freeboard02.api.user;

import com.freeboard02.domain.user.UserEntity;
import com.freeboard02.domain.user.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {

    private String accountId;
    private UserRole role;

    public UserDto(UserEntity userEntity){
        this.role = userEntity.getRole();
        this.accountId = userEntity.getAccountId();
    }

    public static UserDto of(UserEntity userEntity) {
        return new UserDto(userEntity);
    }
}
