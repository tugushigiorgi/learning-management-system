package com.leverx.learningmanagementsystem.course.dto;

import com.leverx.learningmanagementsystem.coursesettings.CourseSettings;
import com.leverx.learningmanagementsystem.lesson.dto.LessonDto;
import com.leverx.learningmanagementsystem.student.dto.StudentDto;
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
