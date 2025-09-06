package com.UserPassportBoot.DAO;


import com.UserPassportBoot.model.Passport;
import com.UserPassportBoot.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PassportMapper implements RowMapper<Passport> {
    @Override
    public Passport mapRow(ResultSet resultSet, int i) throws SQLException {
Passport passport = new Passport();
passport.setId(resultSet.getInt(1));
passport.setSerial(resultSet.getString(2));
passport.setNumber(resultSet.getString(3));
passport.setOwner((User)resultSet.getObject(4));
passport.setDateOfReceipt(resultSet.getDate(6).toLocalDate());
return passport;

    }
}
