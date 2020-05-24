package com.freeboard02.domain.user;

import com.freeboard02.domain.BaseEntity;
import com.freeboard02.domain.user.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    private String accountId;

    private String password;

    @Setter
    private UserRole role;

    @Builder
    public UserEntity(String accountId, String password, UserRole role) {
        this.accountId = accountId;
        this.password = password;
        this.role = role;
    }

}
