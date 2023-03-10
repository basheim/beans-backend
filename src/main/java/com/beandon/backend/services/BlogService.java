package com.beandon.backend.services;

import com.beandon.backend.pojo.blog.PostData;
import com.beandon.backend.pojo.blog.PostPageData;
import com.beandon.backend.pojo.blog.PreviewData;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class BlogService {

    private static final String S3_BUCKET_NAME = "beans-post-text";
    private static final int PREVIEW_LIMIT = 8;
    private final JdbcTemplate jdbcTemplate;
    private final FileService fileService;
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;

    public BlogService(DataSource dataSource, FileService fileService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.fileService = fileService;
        this.markdownParser = Parser.builder().build();
        this.htmlRenderer = HtmlRenderer.builder().build();
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
                        "SELECT * FROM posts WHERE id=?;",
                        new BeanPropertyRowMapper<>(PostData.class),
                        id);
        if (mainPost == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post id could not be found.");
        }

        String data = fileService.getText(S3_BUCKET_NAME, mainPost.getContent());
        List<Object> parameters = mainPost.getTags().stream().map(s -> "%" + s + "%").collect(Collectors.toList());
        parameters.add(mainPost.getId());
        parameters.add(PREVIEW_LIMIT);

        List<PreviewData> previewData =
                jdbcTemplate.query(
                        "SELECT id, title, description, createdDate, tags FROM posts " +
                                "WHERE " +
                                getLikeQuery(mainPost.getTags().size()) +
                                "AND NOT id=? " +
                                "LIMIT ?;",
                        new BeanPropertyRowMapper<>(PreviewData.class),
                        parameters.toArray());
        return PostPageData.builder()
                .post(mainPost)
                .previews(previewData)
                .data(data)
                .build();
    }

    private String getLikeQuery(int tagCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < tagCount; i++) {
            sb.append("tags LIKE ? ");
            if (i < tagCount - 1) {
                sb.append("OR ");
            }
        }
        sb.append(")");
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
        jdbcTemplate.update("DELETE FROM posts " +
                "WHERE id=? " +
                "LIMIT 1;", id);
    }

    public void deleteAllPosts() {
        jdbcTemplate.update("DELETE FROM posts;");
    }

    public URL savePost(MultipartFile multipartFile, String fileName) {
        try {
            Node document = markdownParser.parse(new String(multipartFile.getBytes(), StandardCharsets.UTF_8));
            String html = htmlRenderer.render(document);
            return fileService.save(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), S3_BUCKET_NAME, fileName);
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File can not be converted to html.");
        }
    }
}
