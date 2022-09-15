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


@Service
public class BlogService {

    private static final int PREVIEW_LIMIT = 8;
    private final JdbcTemplate jdbcTemplate;

    public BlogService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<PreviewData> getAllPreviews() {
        return jdbcTemplate.query(
                "SELECT id, title, description, createdDate, tag FROM posts;",
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
        List<PreviewData> previewData =
                jdbcTemplate.query(
                        String.format("SELECT id, title, description, createdDate, tag FROM posts " +
                                "WHERE tag='%s' " +
                                "AND NOT id='%s' " +
                                "LIMIT %d;", mainPost.getTag(), mainPost.getId(), PREVIEW_LIMIT),
                        new BeanPropertyRowMapper<>(PreviewData.class));
        return PostPageData.builder()
                .post(mainPost)
                .previews(previewData)
                .build();
    }

    public void writePost(PostData post) {
        jdbcTemplate.update(
                "INSERT INTO posts (id,title,description,author,content,tag,createdDate) VALUES (?,?,?,?,?,?,?);",
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getAuthor(),
                post.getContent(),
                post.getTag(),
                post.getCreatedDate()
        );
    }

    public void deletePost(String id) {
        jdbcTemplate.update(String.format("DELETE FROM posts " +
                "WHERE id='%s' " +
                "LIMIT 1;", id));
    }
}