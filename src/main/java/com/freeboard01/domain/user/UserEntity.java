package com.freeboard01.domain.user;

import com.freeboard01.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Column
    private String accountId;

    @Column
    private String password;

    @Builder
    public UserEntity(String accountId, String password){
        this.accountId = accountId;
        this.password = password;
    }

}
