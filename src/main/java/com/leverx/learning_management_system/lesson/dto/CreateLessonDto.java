package com.leverx.learning_management_system.lesson.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateLessonDto {
  @NotBlank(message = "title is required")
  @Size(min = 1, max = 100, message = "title must be between 1 and 100 characters")
  private String title;

  @NotNull(message = "duration is required")
  @Positive(message = "duration must be a positive number (in minutes)")
  private Integer duration;
}
