package com.leverx.learning_management_system.student.service;

import com.leverx.learning_management_system.student.dto.CreateStudentDto;
import com.leverx.learning_management_system.student.dto.StudentDto;
import com.leverx.learning_management_system.student.dto.UpdateStudentDto;
import java.util.List;
import java.util.UUID;

public interface StudentService {
  Integer getEnrolledCourseCount(UUID studentId);

  void enrollToCourse(UUID studentId, UUID courseId);

  StudentDto createStudent(CreateStudentDto studentDto);

  StudentDto getStudentById(UUID id);

  List<StudentDto> getAllStudents();

  void deleteById(UUID id);

  StudentDto updateStudent(UpdateStudentDto studentDto);
}
