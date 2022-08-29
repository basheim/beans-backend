package com.beandon.backend.controllers;

import com.beandon.backend.pojo.Plant;
import com.beandon.backend.services.ForageleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ForageleController {

    private final ForageleService forageleService;

    @GetMapping("/plants")
    public List<Plant> getAllPlants() {
        return forageleService.getAllPlants();
    }

    @GetMapping("/plants/{id}")
    public Plant getPlant(@PathVariable("id") String id) {
        return forageleService.getPlant(id);
    }

    @PostMapping("/plants")
    public void importPlant(@RequestBody Plant plant) {
        forageleService.writePlant(plant);
    }
}
