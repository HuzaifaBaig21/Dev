package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.AssignmentCreateRequest;
import com.nimblix.SchoolPEPProject.Request.AssignmentUpdateRequest;
import com.nimblix.SchoolPEPProject.Response.AssignmentResponse;
import com.nimblix.SchoolPEPProject.Service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentResponse> createAssignment(
            @Valid @RequestBody AssignmentCreateRequest request) {
        AssignmentResponse response = assignmentService.createAssignment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponse> updateAssignment(
            @PathVariable Long assignmentId,
            @Valid @RequestBody AssignmentUpdateRequest request) {
        AssignmentResponse response = assignmentService.updateAssignment(assignmentId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponse> getAssignment(@PathVariable Long assignmentId) {
        AssignmentResponse response = assignmentService.getAssignmentById(assignmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsByTeacher(@PathVariable Long teacherId) {
        List<AssignmentResponse> responses = assignmentService.getAssignmentsByTeacher(teacherId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsByClass(@PathVariable Long classId) {
        List<AssignmentResponse> responses = assignmentService.getAssignmentsByClass(classId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }
}