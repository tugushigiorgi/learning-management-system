package com.leverx.learning_management_system.lesson.dto;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLessonDto {
  private String title;
  private Integer duration;
}
