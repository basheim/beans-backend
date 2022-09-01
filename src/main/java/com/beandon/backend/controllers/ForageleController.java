package com.beandon.backend.controllers;

import com.beandon.backend.pojo.CompletePlantData;
import com.beandon.backend.services.ForageleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ForageleController {

    private final ForageleService forageleService;

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
}
