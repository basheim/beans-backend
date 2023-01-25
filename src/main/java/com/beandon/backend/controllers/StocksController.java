package com.beandon.backend.controllers;

import com.beandon.backend.pojo.stocks.StockData;
import com.beandon.backend.pojo.stocks.TransactionData;
import com.beandon.backend.services.StocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StocksController {

    private final StocksService stocksService;

    @GetMapping("/stocks")
    public List<StockData> getAllStocks() {
        return stocksService.getAllStocks();
    }

    @GetMapping("/transactions")
    public List<TransactionData> getAllTransactions(@RequestParam(required = false) Long date) {
        if (date == null) {
            date = Instant.now().minusSeconds(60 * 60 * 24 * 7).toEpochMilli();
        }
        return stocksService.getAllTransactions(Timestamp.from(Instant.ofEpochMilli(date)));
    }
}
