package com.beandon.backend.controllers;

import com.beandon.backend.pojo.S3Content;
import com.beandon.backend.pojo.foragele.CompletePlantData;
import com.beandon.backend.pojo.foragele.PlantLatestDate;
import com.beandon.backend.services.FileService;
import com.beandon.backend.services.ForageleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ForageleController {

    private static final String S3_BUCKET_NAME = "foragele-images";
    private static final String RAW_S3_BUCKET_NAME = "foragele-raw-images";

    private final ForageleService forageleService;
    private final FileService fileService;

    @GetMapping("/plants")
    public List<CompletePlantData> getAllPlants() {
        return forageleService.getAllPlants();
    }

    @GetMapping("/plants/{id}")
    public CompletePlantData getPlant(@PathVariable("id") String id) {
        return forageleService.getPlant(id);
    }

    @PostMapping("/plants")
    public void importPlant(@RequestBody CompletePlantData plant) {
        forageleService.writePlant(plant);
    }

    @DeleteMapping("/plants")
    public void deleteAllPlants() {
        forageleService.deleteAllPlants();
    }

    @GetMapping("/plants/latestDate")
    public PlantLatestDate getPlantsLatestDate() {
        return forageleService.getLatestDate();
    }

    @PostMapping("/plants/image/save")
    public S3Content saveImage(@RequestParam("file") @NonNull MultipartFile multipartFile, @RequestParam("name") @NonNull String fileName) {
        URL url = fileService.save(fileService.multiPartFileToStream(multipartFile), S3_BUCKET_NAME, fileName);
        return S3Content.builder()
                .url(url.toString())
                .build();
    }

    @PostMapping("/plants/raw/image/save")
    public S3Content saveRawImage(@RequestParam("file") @NonNull MultipartFile multipartFile, @RequestParam("name") @NonNull String fileName) {
        URL url = fileService.save(fileService.multiPartFileToStream(multipartFile), RAW_S3_BUCKET_NAME, fileName);
        return S3Content.builder()
                .url(url.toString())
                .build();
    }
}
