package com.example.academiaindustryportal.service;

import com.example.academiaindustryportal.dto.ProjectDto;
import com.example.academiaindustryportal.entity.Project;
import com.example.academiaindustryportal.entity.User;
import com.example.academiaindustryportal.repository.ProjectRepository;
import com.example.academiaindustryportal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectDto createProject(ProjectDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project(dto.getTitle(), dto.getDescription(), dto.getDomain(), user);
        Project saved = projectRepository.save(project);
        return mapToDto(saved);
    }

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private ProjectDto mapToDto(Project p) {
        ProjectDto dto = new ProjectDto();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setDescription(p.getDescription());
        dto.setDomain(p.getDomain());
        dto.setPostedByEmail(p.getPostedBy().getEmail());
        return dto;
    }
}