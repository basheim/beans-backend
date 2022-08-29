package com.beandon.backend.controllers;

import com.beandon.backend.services.ForageleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ForageleController {

    private final ForageleService forageleService;

    @GetMapping("/plants")
    public List<Map<String, Object>> getAllPlants() {
        return forageleService.getAllPlants();
    }
}
