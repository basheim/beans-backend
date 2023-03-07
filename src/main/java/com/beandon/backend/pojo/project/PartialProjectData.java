package com.beandon.backend.pojo.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartialProjectData {
    private String name;
    private int progress;
    private String state;
    private String link;
}
