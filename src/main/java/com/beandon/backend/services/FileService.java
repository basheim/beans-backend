package com.beandon.backend.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 amazonS3;

    public URL save(final MultipartFile multipartFile, final String s3BucketName, final String fileName) {
        try {
            URL url = amazonS3.getUrl(s3BucketName, fileName);
            try {
                amazonS3.getObject(s3BucketName, fileName);
            } catch (AmazonServiceException e) {
                final PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, multipartFile.getInputStream(), new ObjectMetadata());
                amazonS3.putObject(putObjectRequest);
            }
            return url;
        }  catch (IOException ex) {
            throw new IllegalArgumentException("File could not be written. " + ex.getMessage());
        }
    }

    public String getText(final String s3BucketName, final String fileName) {
        final GetObjectRequest req = new GetObjectRequest(s3BucketName, fileName);
        try {
            final byte[] object = amazonS3.getObject(req).getObjectContent().readAllBytes();
            return new String(object, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalArgumentException("File could not be read.");
        }
    }
}
