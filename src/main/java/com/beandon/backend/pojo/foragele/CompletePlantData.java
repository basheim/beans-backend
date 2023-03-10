package com.beandon.backend.pojo.foragele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompletePlantData {
    @NonNull
    private String id;
    @NonNull
    private String poisonousLookAlike;
    @NonNull
    private String edibility;
    @NonNull
    private String foundNear;
    @NonNull
    private String keyFeatures;
    @NonNull
    private String imageUrl;
    @NonNull
    private String english;
    @NonNull
    private String latin;
    private Timestamp start;
    private Timestamp end;
}
