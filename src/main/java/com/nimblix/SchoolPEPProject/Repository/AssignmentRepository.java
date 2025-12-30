package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Assignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignments, Long> {
    List<Assignments> findByTeacher_TeacherId(Long teacherId);
    List<Assignments> findByClassroom_ClassroomId(Long classroomId);
    List<Assignments> findByTeacher_TeacherIdAndClassroom_ClassroomId(Long teacherId, Long classroomId);
}