package com.beandon.backend.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SelectedPlant {
    private String id;
    private Timestamp start;
    private Timestamp end;
}
