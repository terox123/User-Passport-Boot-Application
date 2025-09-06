package com.UserPassportBoot.DAO;


import com.UserPassportBoot.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
User user = new User();
user.setId(rs.getInt(1));
user.setName(rs.getString(2));
user.setEmail(rs.getString(3));
user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
user.setDateOfBirth(rs.getTimestamp(5).toLocalDateTime().toLocalDate());
user.setGender(rs.getString(6));
return user;
    }
}
