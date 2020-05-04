package com.freeboard01.api.user;

import com.freeboard01.domain.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {

    private String accountId;

    public UserDto(UserEntity userEntity){
        this.accountId = userEntity.getAccountId();
    }

    public static UserDto of(UserEntity userEntity) {
        return new UserDto(userEntity);
    }
}
