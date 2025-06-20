package com.leverx.learningmanagementsystem.course.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class PatchCourseDto implements CourseUpdateDto {

  @Nullable
  @Size(min = 1, max = 100, message = "title must be between 1 and 100 characters")
  private String title;

  @Nullable
  @Size(min = 1, max = 1000, message = "description must be between 1 and 1000 characters")
  private String description;

  @Nullable
  @Positive(message = "price must be greater than 0")
  private BigDecimal price;

  @Nullable
  @PositiveOrZero(message = "coinsPaid must be zero or positive")
  private BigDecimal coinsPaid;

  @Nullable
  @FutureOrPresent(message = "startDate must be in the present or future")
  private LocalDateTime startDate;

  @Nullable
  @Future(message = "endDate must be in the future")
  private LocalDateTime endDate;

  @Nullable
  private Boolean isPublic;
}