package com.beandon.backend.pojo;

import lombok.Data;
@Data
public class Plant {
    private String id;
    private String name;
    private String poisonousLookAlike;
    private String edibility;
    private String foundNear;
    private String keyFeatures;
    private String imageUrl;
}
