package com.beandon.backend.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 amazonS3;

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("File could not be written.");
        }
        return file;
    }

    public URL save(final MultipartFile multipartFile, final String s3BucketName) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            URL url = amazonS3.getUrl(s3BucketName, file.getName());
            try {
                amazonS3.getObject(s3BucketName, file.getName());
            } catch (AmazonServiceException e) {
                final PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, file.getName(), file);
                amazonS3.putObject(putObjectRequest);
            }
            Files.delete(file.toPath()); // Remove the file locally created in the project folder
            return url;
        }  catch (IOException ex) {
            throw new IllegalArgumentException("File could not be written.");
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
