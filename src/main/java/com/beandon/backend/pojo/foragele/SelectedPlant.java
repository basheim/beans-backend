package com.beandon.backend.pojo.foragele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectedPlant {
    private String id;
    private Timestamp start;
    private Timestamp end;
}
