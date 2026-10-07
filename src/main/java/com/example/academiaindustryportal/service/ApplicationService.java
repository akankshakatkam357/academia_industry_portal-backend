package com.example.academiaindustryportal.service;

import com.example.academiaindustryportal.dto.ApplicationDto;
import com.example.academiaindustryportal.entity.*;
import com.example.academiaindustryportal.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final InternshipRepository internshipRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              UserRepository userRepository,
                              ProjectRepository projectRepository,
                              InternshipRepository internshipRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.internshipRepository = internshipRepository;
    }

    public ApplicationDto apply(ApplicationDto dto, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Application app = new Application();
        app.setStudent(student);
        app.setCoverLetter(dto.getCoverLetter());

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            app.setProject(project);
        } else if (dto.getInternshipId() != null) {
            Internship internship = internshipRepository.findById(dto.getInternshipId())
                    .orElseThrow(() -> new RuntimeException("Internship not found"));
            app.setInternship(internship);
        } else {
            throw new RuntimeException("Application must target either a Project or an Internship");
        }

        Application saved = applicationRepository.save(app);
        return mapToDto(saved);
    }

    public List<ApplicationDto> getStudentApplications(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return applicationRepository.findByStudentId(student.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ApplicationDto updateStatus(Long applicationId, ApplicationStatus status) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(status);
        Application updated = applicationRepository.save(app);
        return mapToDto(updated);
    }

    private ApplicationDto mapToDto(Application a) {
        ApplicationDto dto = new ApplicationDto();
        dto.setId(a.getId());
        dto.setStudentId(a.getStudent().getId());
        dto.setStudentEmail(a.getStudent().getEmail());
        if (a.getProject() != null) dto.setProjectId(a.getProject().getId());
        if (a.getInternship() != null) dto.setInternshipId(a.getInternship().getId());
        dto.setCoverLetter(a.getCoverLetter());
        dto.setStatus(a.getStatus());
        return dto;
    }
}
