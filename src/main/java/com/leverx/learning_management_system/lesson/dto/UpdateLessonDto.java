package com.leverx.learning_management_system.lesson.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class UpdateLessonDto {
  private UUID id;
  private String title;
  private Integer duration;
}
