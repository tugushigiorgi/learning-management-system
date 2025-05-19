package com.leverx.learning_management_system.lesson.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLessonDto {
  private UUID id;
  private String title;
  private Integer duration;
}
