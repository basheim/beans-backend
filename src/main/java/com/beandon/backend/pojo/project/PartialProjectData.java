package com.beandon.backend.pojo.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartialProjectData {
    @NonNull
    private String name;
    @NonNull
    private Integer progress;
    @NonNull
    private String state;
    @NonNull
    private String link;
}
