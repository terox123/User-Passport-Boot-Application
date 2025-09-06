package com.UserPassportBoot.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setIDForDelete(Integer maxId) {
        int newAutoIncrementValue = (maxId == null) ? 1 : maxId + 1;
        jdbcTemplate.update("ALTER TABLE java_users AUTO_INCREMENT = ?", newAutoIncrementValue);
    }
}