package com.beandon.backend.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class ForageleFileService {

    private static final String S3_BUCKET_NAME = "foragele-images";

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

    public URL save(final MultipartFile multipartFile) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            URL url = amazonS3.getUrl(S3_BUCKET_NAME, file.getName());
            try {
                amazonS3.getObject(S3_BUCKET_NAME, file.getName());
            } catch (AmazonServiceException e) {
                final PutObjectRequest putObjectRequest = new PutObjectRequest(S3_BUCKET_NAME, file.getName(), file);
                amazonS3.putObject(putObjectRequest);
            }
            Files.delete(file.toPath()); // Remove the file locally created in the project folder
            return url;
        }  catch (IOException ex) {
            throw new IllegalArgumentException("File could not be written.");
        }
    }
}
