package com.beandon.backend.services;

import com.beandon.backend.pojo.foragele.CompletePlantData;
import com.beandon.backend.pojo.stocks.AccountData;
import com.beandon.backend.pojo.stocks.AccountOverview;
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

    private static final String INITIAL_ACCOUNT = "initial";
    private static final String CURRENT_ACCOUNT = "current";

    public StocksService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<StockData> getAllStocks() {
        return jdbcTemplate.query(
                "SELECT * FROM stocks;",
                new BeanPropertyRowMapper<>(StockData.class));
    }

    public List<TransactionData> getAllTransactions(Timestamp date) {
        return jdbcTemplate.query(
                String.format("SELECT * FROM stock_transactions " +
                        "WHERE date > '%s';", date.toString()),
                new BeanPropertyRowMapper<>(TransactionData.class));
    }

    public AccountOverview getAccountOverview() {
        AccountData initial = jdbcTemplate.queryForObject(
                String.format("SELECT * FROM account_status " +
                                "WHERE id='%s';", INITIAL_ACCOUNT),
                new BeanPropertyRowMapper<>(AccountData.class));
        AccountData current = jdbcTemplate.queryForObject(
                String.format("SELECT * FROM account_status " +
                        "WHERE id='%s';", CURRENT_ACCOUNT),
                new BeanPropertyRowMapper<>(AccountData.class));
        return AccountOverview.builder()
                .amount(current.getAmount())
                .percentChange(((current.getAmount() - initial.getAmount()) / initial.getAmount()) * 100)
                .build();
    }
}
