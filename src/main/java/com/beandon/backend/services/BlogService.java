package com.beandon.backend.services;

import com.beandon.backend.pojo.blog.PostData;
import com.beandon.backend.pojo.blog.PostPageData;
import com.beandon.backend.pojo.blog.PreviewData;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;


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

    public URL savePost(MultipartFile multipartFile, String fileName) {
        try {
            Node document = markdownParser.parse(new String(multipartFile.getBytes(), StandardCharsets.UTF_8));
            String html = htmlRenderer.render(document);
            return fileService.save(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), S3_BUCKET_NAME, fileName);
        } catch(IOException e) {
            throw new IllegalStateException("File can not be converted to html.");
        }
    }
}
