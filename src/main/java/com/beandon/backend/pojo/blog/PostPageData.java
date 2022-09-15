package com.beandon.backend.pojo.blog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostPageData {
    private PostData post;
    private List<PreviewData> previews;
}
