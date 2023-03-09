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
                "SELECT * FROM stock_transactions " +
                        "WHERE date > '%s' ORDER BY date DESC;",
                new BeanPropertyRowMapper<>(TransactionData.class),
                date.toString());
    }

    public AccountOverview getAccountOverview() {
        AccountData initial = jdbcTemplate.queryForObject(
                "SELECT * FROM account_status " +
                        "WHERE id='%s';",
                new BeanPropertyRowMapper<>(AccountData.class),
                INITIAL_ACCOUNT);
        AccountData current = jdbcTemplate.queryForObject(
                "SELECT * FROM account_status " +
                        "WHERE id='%s';",
                new BeanPropertyRowMapper<>(AccountData.class),
                CURRENT_ACCOUNT);
        return AccountOverview.builder()
                .amount(current.getAmount())
                .percentChange(((current.getAmount() - initial.getAmount()) / initial.getAmount()) * 100)
                .build();
    }
}
