package com.beandon.backend.services;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;


@Service
public class ForageleService {

    private final JdbcTemplate jdbcTemplate;

    public ForageleService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> getAllPlants() {
        return jdbcTemplate.queryForList("SELECT * FROM testing");
    }
}
