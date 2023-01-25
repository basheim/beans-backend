package com.beandon.backend.controllers;

import com.beandon.backend.pojo.stocks.StockData;
import com.beandon.backend.services.StocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
