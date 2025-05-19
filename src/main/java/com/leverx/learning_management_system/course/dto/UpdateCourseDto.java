package com.leverx.learning_management_system.course.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class UpdateCourseDto {

  @NotNull(message = "Course ID must be provided")
  private UUID id;

  @Size(min = 1, max = 100, message = "title must be between 1 and 100 characters")
  private String title;

  @Size(min = 1, max = 1000, message = "description must be between 1 and 1000 characters")
  private String description;

  @Positive(message = "price must be greater than 0")
  private BigDecimal price;

  @PositiveOrZero(message = "coinsPaid must be zero or positive")
  private BigDecimal coinsPaid;

  @FutureOrPresent(message = "startDate must be in the present or future")
  private LocalDateTime startDate;

  @Future(message = "endDate must be in the future")
  private LocalDateTime endDate;

  private Boolean isPublic;
}