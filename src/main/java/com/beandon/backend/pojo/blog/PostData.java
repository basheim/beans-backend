package com.beandon.backend.pojo.blog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostData {
    @NonNull
    private String title;
    @NonNull
    private String id;
    @NonNull
    private String description;
    @NonNull
    private Timestamp createdDate;
    @NonNull
    private String content;
    @NonNull
    private String author;
    private List<String> tags;
    private String prev;
    private String next;

    @JsonSetter
    public void setTags(String tags) {
        this.tags = Arrays.asList(tags.split(","));
    }
}
