package com.beandon.backend.controllers;

import com.beandon.backend.pojo.CompletePlantData;
import com.beandon.backend.pojo.PlantImage;
import com.beandon.backend.pojo.PlantLatestDate;
import com.beandon.backend.services.ForageleFileService;
import com.beandon.backend.services.ForageleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ForageleController {

    private final ForageleService forageleService;
    private final ForageleFileService fileService;

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
    public PlantImage saveImage(@RequestParam("file") MultipartFile multipartFile) {
        URL url = fileService.save(multipartFile);
        return PlantImage.builder()
                .url(url.toString())
                .build();
    }
}
