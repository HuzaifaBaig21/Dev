package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Exception.UserNotFoundException;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.*;
import com.nimblix.SchoolPEPProject.Request.AssignmentCreateRequest;
import com.nimblix.SchoolPEPProject.Request.AssignmentUpdateRequest;
import com.nimblix.SchoolPEPProject.Response.AssignmentResponse;
import com.nimblix.SchoolPEPProject.Service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    @Override
    @Transactional
    public AssignmentResponse createAssignment(AssignmentCreateRequest request) {
        // Validate teacher
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new UserNotFoundException("Teacher not found with ID: " + request.getTeacherId()));


        Classroom classroom = classroomRepository.findById(request.getClassId())
                .orElseThrow(() -> new UserNotFoundException("Classroom not found with ID: " + request.getClassId()));


        Assignments assignment = new Assignments();
        assignment.setTeacher(teacher);
        assignment.setClassroom(classroom);

        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());


        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            List<Attachments> attachments = request.getAttachments().stream()
                    .map(meta -> {
                        Attachments attachment = new Attachments();

                        attachment.setAssignment(assignment);
                        return attachment;
                    })
                    .collect(Collectors.toList());
            assignment.setAttachments(attachments);
        }

        Assignments saved = assignmentRepository.save(assignment);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public AssignmentResponse updateAssignment(Long assignmentId, AssignmentUpdateRequest request) {
        Assignments assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new UserNotFoundException("Assignment not found with ID: " + assignmentId));


        if (LocalDateTime.now().isAfter(assignment.getDueDate())) {
            throw new IllegalStateException("Cannot edit assignment after due date");
        }

        // Update fields if provided
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            assignment.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            assignment.setDescription(request.getDescription());
        }

        if (request.getDueDate() != null) {
            assignment.setDueDate(request.getDueDate());
        }

        Assignments updated = assignmentRepository.save(assignment);
        return mapToResponse(updated);
    }

    @Override
    public AssignmentResponse getAssignmentById(Long assignmentId) {
        Assignments assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new UserNotFoundException("Assignment not found with ID: " + assignmentId));
        return mapToResponse(assignment);
    }

    @Override
    public List<AssignmentResponse> getAssignmentsByTeacher(Long teacherId) {
        List<Assignments> assignments = assignmentRepository.findByTeacher_TeacherId(teacherId);
        return assignments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentResponse> getAssignmentsByClass(Long classId) {
        List<Assignments> assignments = assignmentRepository.findByClassroom_ClassroomId(classId);
        return assignments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAssignment(Long assignmentId) {
        if (!assignmentRepository.existsById(assignmentId)) {
            throw new UserNotFoundException("Assignment not found with ID: " + assignmentId);
        }
        assignmentRepository.deleteById(assignmentId);
    }

    private AssignmentResponse mapToResponse(Assignments assignment) {
        List<AssignmentResponse.AttachmentInfo> attachmentInfos = assignment.getAttachments().stream()
                .map(att -> AssignmentResponse.AttachmentInfo.builder()

                        .build())
                .collect(Collectors.toList());

        return AssignmentResponse.builder()
                .assignmentId(assignment.getAssignmentId())
                .teacherId(assignment.getTeacher().getTeacherId())
                .teacherName(assignment.getTeacher().getUser().getName()) // Adjust based on your Teacher model
                .classId(assignment.getClassroom().getClassroomId())
                .className(assignment.getClassroom().getClassName()) // Adjust field name
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .dueDate(assignment.getDueDate())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .attachments(attachmentInfos)
                .build();
    }
}