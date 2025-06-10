package com.leverx.learningmanagementsystem.lesson.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLessonDto {

  @Nullable
  @Size(min = 1, max = 100, message = "title must be between 1 and 100 characters")
  private String title;

  @Nullable
  @Positive(message = "duration must be a positive number (in minutes)")
  private Integer duration;
}
