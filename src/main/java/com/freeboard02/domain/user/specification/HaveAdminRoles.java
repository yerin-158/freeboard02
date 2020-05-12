package com.freeboard02.domain.user.specification;

import com.freeboard02.domain.user.UserEntity;
import com.freeboard02.domain.user.enums.UserRole;

import java.util.Arrays;
import java.util.List;

public class HaveAdminRoles {

    static List<UserRole> upperRoleList;

    static {
        upperRoleList = Arrays.asList(UserRole.ADMIN);
    }

    public static boolean confirm(UserEntity user) {
        return upperRoleList.stream().anyMatch(role -> role.equals(user.getRole()));
    }
}
