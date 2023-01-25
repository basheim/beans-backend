package com.beandon.backend.pojo.stocks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockData {
    private String id;
    private String name;
    private String code;
    private Timestamp date;
    private int price;
    private int quantity;
}
