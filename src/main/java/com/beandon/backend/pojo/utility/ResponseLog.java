package com.beandon.backend.pojo.utility;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLog {
    private int code;
    private Object payload;
    private long executionTime;
}
