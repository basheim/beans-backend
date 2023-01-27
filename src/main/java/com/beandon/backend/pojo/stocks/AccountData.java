package com.beandon.backend.pojo.stocks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountData {
    private String id;
    private float amount;
}
