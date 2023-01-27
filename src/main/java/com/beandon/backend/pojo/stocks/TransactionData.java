package com.beandon.backend.pojo.stocks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionData {
    private String id;
    private String name;
    private String stockId;
    private String action;
    private float price;
    private float quantity;
    private Timestamp date;
}
