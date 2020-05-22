package com.freeboard02.util.typeHandler;

import com.freeboard02.domain.user.enums.UserRole;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleTypeHandler<E extends Enum<E>> implements TypeHandler<UserRole> {

    private Class<E> type;

    public UserRoleTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, UserRole parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public UserRole getResult(ResultSet rs, String columnName) throws SQLException {
        String role = rs.getString(columnName);
        return getUserRole(role);
    }

    @Override
    public UserRole getResult(ResultSet rs, int columnIndex) throws SQLException {
        String role = rs.getString(columnIndex);
        return getUserRole(role);
    }

    @Override
    public UserRole getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String role = cs.getString(columnIndex);
        return getUserRole(role);
    }

    private UserRole getUserRole(String role) {
        UserRole[] userRoles = (UserRole[]) type.getEnumConstants();
        for (UserRole userRole : userRoles) {
            if (userRole.name().equals(role))
                return userRole;
        }
        throw new IllegalArgumentException("No enum constant " + type.getCanonicalName() + "." + role);
    }
}
