package com.leverx.learningmanagementsystem.course.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
public class ReplaceCourseDto implements CourseUpdateDto {

  @NotNull(message = "title must not be null")
  @Size(min = 1, max = 100, message = "title must be between 1 and 100 characters")
  private String title;

  @NotNull(message = "description must not be null")
  @Size(min = 1, max = 1000, message = "description must be between 1 and 1000 characters")
  private String description;

  @NotNull(message = "price must not be null")
  @Positive(message = "price must be greater than 0")
  private BigDecimal price;

  @NotNull(message = "coinsPaid must not be null")
  @PositiveOrZero(message = "coinsPaid must be zero or positive")
  private BigDecimal coinsPaid;

  @NotNull(message = "startDate must not be null")
  @FutureOrPresent(message = "startDate must be in the present or future")
  private LocalDateTime startDate;

  @NotNull(message = "endDate must not be null")
  @Future(message = "endDate must be in the future")
  private LocalDateTime endDate;

  @NotNull(message = "isPublic must not be null")
  private Boolean isPublic;
}
