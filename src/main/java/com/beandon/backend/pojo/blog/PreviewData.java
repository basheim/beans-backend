package com.beandon.backend.pojo.blog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreviewData {
    private String title;
    private String id;
    private String description;
    private Timestamp createdDate;
    private String tag;
}
