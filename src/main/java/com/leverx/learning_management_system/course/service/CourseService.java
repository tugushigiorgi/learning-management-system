package com.leverx.learning_management_system.course.service;

import com.leverx.learning_management_system.course.dto.CourseDto;
import com.leverx.learning_management_system.course.dto.CreateCourseDto;
import com.leverx.learning_management_system.course.dto.DetailedCourseDto;
import com.leverx.learning_management_system.course.dto.UpdateCourseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CourseService {
  void printAlCourseByStartDateBetween(LocalDateTime start, LocalDateTime end);
  List<CourseDto> mostPopularCourses();
  void createCourse(CreateCourseDto courseDto);
  CourseDto getCourseById(UUID id);
  List<CourseDto> getAllCourses();
  void deleteById(UUID id);
  void updateCourse(UpdateCourseDto courseDto);
  DetailedCourseDto getDetailedCourseById(UUID id);
  void sendMailToEnrolledStudents(UUID courseId);
}
