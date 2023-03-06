package com.beandon.backend.services;


import com.beandon.backend.pojo.blog.PostData;
import com.beandon.backend.pojo.blog.PostPageData;
import com.beandon.backend.pojo.blog.PreviewData;
import com.beandon.backend.pojo.foragele.CompletePlantData;
import com.beandon.backend.pojo.foragele.PlantLatestDate;
import com.beandon.backend.pojo.foragele.SelectedPlant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BlogService {

    private static final String S3_BUCKET_NAME = "beans-post-text";
    private static final int PREVIEW_LIMIT = 8;
    private final JdbcTemplate jdbcTemplate;
    private final FileService fileService;

    public BlogService(DataSource dataSource, FileService fileService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.fileService = fileService;
    }

    public List<PreviewData> getAllPreviews() {
        return jdbcTemplate.query(
                "SELECT id, title, description, createdDate, tags FROM posts;",
                new BeanPropertyRowMapper<>(PreviewData.class));
    }

    public List<String> getAllIds() {
        return jdbcTemplate.queryForList(
                "SELECT id FROM posts;", String.class);
    }

    public PostPageData getCompletePost(String id) {
        PostData mainPost =
                jdbcTemplate.queryForObject(
                    String.format("SELECT * FROM posts " +
                            "WHERE id='%s';", id),
                    new BeanPropertyRowMapper<>(PostData.class));
        if (mainPost == null) {
            throw new IllegalStateException("Main post not found");
        }

        String data = fileService.getText(S3_BUCKET_NAME, mainPost.getContent());

        List<PreviewData> previewData =
                jdbcTemplate.query(
                        String.format("SELECT id, title, description, createdDate, tags FROM posts " +
                                "WHERE (%s) " +
                                "AND NOT id='%s' " +
                                "LIMIT %d;", getContainsString(mainPost.getTags(), "tags"), mainPost.getId(), PREVIEW_LIMIT),
                        new BeanPropertyRowMapper<>(PreviewData.class));
        return PostPageData.builder()
                .post(mainPost)
                .previews(previewData)
                .data(data)
                .build();
    }

    private String getContainsString(List<String> tags, String column) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tags.size(); i++) {
            if (i != 0) {
                sb.append("OR ");
            }
            sb.append(column);
            sb.append(" ");
            sb.append("LIKE '%");
            sb.append(tags.get(i));
            sb.append("%' ");
        }
        return sb.toString();
    }

    public void writePost(PostData post) {
        jdbcTemplate.update(
                "INSERT INTO posts (id,title,description,author,content,tags,createdDate,prev,next) VALUES (?,?,?,?,?,?,?,?,?);",
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getAuthor(),
                post.getContent(),
                String.join(",", post.getTags()),
                post.getCreatedDate(),
                post.getPrev(),
                post.getNext()
        );
    }

    public void deletePost(String id) {
        jdbcTemplate.update(String.format("DELETE FROM posts " +
                "WHERE id='%s' " +
                "LIMIT 1;", id));
    }

    public void deleteAllPosts() {
        jdbcTemplate.update("DELETE FROM posts;");
    }
}
