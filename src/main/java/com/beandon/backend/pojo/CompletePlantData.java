package com.beandon.backend.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompletePlantData {
    private String id;
    private String poisonousLookAlike;
    private String edibility;
    private String foundNear;
    private String keyFeatures;
    private String imageUrl;
    private String english;
    private String latin;
    private Timestamp start;
    private Timestamp end;
}
