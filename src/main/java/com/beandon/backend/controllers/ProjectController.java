package com.beandon.backend.controllers;

import com.beandon.backend.pojo.project.PartialProjectData;
import com.beandon.backend.pojo.project.ProjectData;
import com.beandon.backend.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/projects")
    public List<ProjectData> getAllProjects() {
        return projectService.getAllProjects();
    }

    @DeleteMapping("/projects/{id}")
    public void deleteProject(@PathVariable("id") String id) {
        projectService.deleteProject(id);
    }

    @PutMapping("/projects/{id}")
    public void updateProject(@PathVariable("id") String id, @RequestBody PartialProjectData data) {
        projectService.updateProject(data, id);
    }

    @PostMapping("/projects")
    public void createProject(@RequestBody PartialProjectData data) {
        projectService.createProject(data);
    }
}
