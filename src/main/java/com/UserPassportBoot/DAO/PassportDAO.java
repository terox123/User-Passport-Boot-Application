package com.UserPassportBoot.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PassportDAO {
private final JdbcTemplate jdbcTemplate;
@Autowired
    public PassportDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setIDAfterDelete(Integer maxId){
        int newAutoIncrementValue = (maxId == null) ? 1 : maxId + 1;

        jdbcTemplate.update("ALTER TABLE passport AUTO_INCREMENT = ?", newAutoIncrementValue);
    }
}
