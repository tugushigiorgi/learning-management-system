package com.leverx.learningmanagementsystem.course.service;

import com.leverx.learningmanagementsystem.course.dto.CourseDto;
import com.leverx.learningmanagementsystem.course.dto.CourseUpdateDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.dto.DetailedCourseDto;
import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CourseService {
  void printAllCoursesByStartDateBetween(LocalDateTime start, LocalDateTime end);

  List<CourseDto> getMostPopularCourses();

  CourseDto createCourse(CreateCourseDto courseDto);

  CourseDto getCourseById(UUID id);

  List<CourseDto> getAllCourses();

  void deleteById(UUID id);

  CourseDto updateCourse(UUID id, CourseUpdateDto courseDto);

  DetailedCourseDto getDetailedCourseById(UUID id);

  void sendMailToEnrolledStudents(UUID courseId) throws MessagingException;
}
