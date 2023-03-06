package com.beandon.backend.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3Content {
    private String url;
}
