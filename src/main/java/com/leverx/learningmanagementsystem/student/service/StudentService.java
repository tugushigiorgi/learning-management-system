package com.leverx.learningmanagementsystem.student.service;

import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentDto;
import com.leverx.learningmanagementsystem.student.dto.UpdateStudentDto;
import jakarta.mail.MessagingException;
import java.util.List;
import java.util.UUID;

public interface StudentService {
  Integer getEnrolledCourseCount(UUID studentId);

  void enrollToCourse(UUID studentId, UUID courseId) throws MessagingException;

  StudentDto createStudent(CreateStudentDto studentDto);

  StudentDto getStudentById(UUID id);

  List<StudentDto> getAllStudents();

  void deleteById(UUID id);

  StudentDto updateStudent(UpdateStudentDto studentDto);

}
