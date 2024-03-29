package com.beandon.backend.pojo.stocks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountOverview {
    private float amount;
    private float percentChange;
}
