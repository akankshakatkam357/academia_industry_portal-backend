package com.example.academiaindustryportal.controller;

import com.example.academiaindustryportal.dto.ApiResponse;
import com.example.academiaindustryportal.dto.ProjectDto;
import com.example.academiaindustryportal.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_FACULTY', 'ROLE_INDUSTRY', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProjectDto>> createProject(@RequestBody ProjectDto dto, Authentication authentication) {
        ProjectDto created = projectService.createProject(dto, authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Project created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectDto>>> getAllProjects() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Projects retrieved", projectService.getAllProjects()));
    }
}