package com.freeboard02.domain.user.specification;

import com.freeboard02.domain.user.UserEntity;

public class IsWriterEqualToUserLoggedIn {

    public static boolean confirm(UserEntity writer, UserEntity loginUser) {
        return writer.getAccountId().equals(loginUser.getAccountId()) ;
    }
}
