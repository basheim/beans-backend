package com.beandon.backend.pojo.stocks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockData {
    private String id;
    private String name;
    private String code;
    private float quantity;
    private float price;
}
