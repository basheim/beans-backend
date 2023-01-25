package com.beandon.backend.pojo.stocks;

import lombok.Builder;

@Builder
public class AccountOverview {
    private int amount;
    private double percentChange;
}
