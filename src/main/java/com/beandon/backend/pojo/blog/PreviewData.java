package com.beandon.backend.pojo.blog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreviewData {
    private String title;
    private String id;
    private String description;
    private Timestamp createdDate;
    private List<String> tags;

    @JsonSetter
    public void setTags(String tags) {
        this.tags = Arrays.asList(tags.split(","));
    }
}
