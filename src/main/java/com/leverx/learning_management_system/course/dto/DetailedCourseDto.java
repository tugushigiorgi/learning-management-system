package com.leverx.learning_management_system.course.dto;

import com.leverx.learning_management_system.courseSettings.CourseSettings;
import com.leverx.learning_management_system.lesson.dto.LessonDto;
import com.leverx.learning_management_system.student.dto.StudentDto;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class DetailedCourseDto {
  private UUID id;
  private String title;
  private String description;
  private BigDecimal price;
  private CourseSettings settings;
  private Set<LessonDto> lessons;
  private Set<StudentDto> students;
}
