package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.AssignmentCreateRequest;
import com.nimblix.SchoolPEPProject.Request.AssignmentUpdateRequest;
import com.nimblix.SchoolPEPProject.Response.AssignmentResponse;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse createAssignment(AssignmentCreateRequest request);
    AssignmentResponse updateAssignment(Long assignmentId, AssignmentUpdateRequest request);
    AssignmentResponse getAssignmentById(Long assignmentId);
    List<AssignmentResponse> getAssignmentsByTeacher(Long teacherId);
    List<AssignmentResponse> getAssignmentsByClass(Long classId);
    void deleteAssignment(Long assignmentId);
}