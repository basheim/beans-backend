package com.beandon.backend.services;

import com.beandon.backend.pojo.stocks.StockData;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class StocksService {
    private final JdbcTemplate jdbcTemplate;

    public StocksService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<StockData> getAllStocks() {
        return jdbcTemplate.query(
                "SELECT * FROM stocks;",
                new BeanPropertyRowMapper<>(StockData.class));
    }
}
