package com.beandon.backend.controllers;

import com.beandon.backend.pojo.S3Content;
import com.beandon.backend.pojo.blog.PostData;
import com.beandon.backend.pojo.blog.PostPageData;
import com.beandon.backend.pojo.blog.PreviewData;
import com.beandon.backend.services.BlogService;
import com.beandon.backend.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BlogController {

    private static final String S3_BUCKET_NAME = "beans-post-text";

    private final BlogService blogService;
    private final FileService fileService;

    @GetMapping("/previews")
    public List<PreviewData> getAllPreviews() {
        return this.blogService.getAllPreviews();
    }

    @GetMapping("/posts/ids")
    public List<String> getAllIds() {
        return this.blogService.getAllIds();
    }

    @GetMapping("/posts/{id}")
    public PostPageData getPost(@PathVariable("id") String id) {
        return this.blogService.getCompletePost(id);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable("id") String id) {
        this.blogService.deletePost(id);
    }

    @PostMapping("/posts")
    public void submitPost(@RequestBody PostData postData) {
        this.blogService.writePost(postData);
    }

    @PostMapping("/posts/save")
    public S3Content saveText(@RequestParam("file") MultipartFile multipartFile, @RequestParam("name") String fileName) {
        URL url = fileService.save(multipartFile, S3_BUCKET_NAME, fileName);
        return S3Content.builder()
                .url(url.toString())
                .build();
    }
}
