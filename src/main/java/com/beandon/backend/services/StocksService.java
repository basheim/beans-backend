package com.beandon.backend.services;

import com.beandon.backend.pojo.stocks.StockData;
import com.beandon.backend.pojo.stocks.TransactionData;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Timestamp;
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

    public List<TransactionData> getAllTransactions(Timestamp date) {
        System.out.println(String.format("SELECT * FROM stock_transactions " +
                "WHERE date > '%s';", date.toString()));
        return jdbcTemplate.query(
                String.format("SELECT * FROM stock_transactions " +
                        "WHERE date > '%s';", date.toString()),
                new BeanPropertyRowMapper<>(TransactionData.class));
    }
}
