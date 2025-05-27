package com.leverx.learningmanagementsystem.course.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class CreateCourseDto {

  @NotBlank(message = "title field is required")
  @Size(min = 1, max = 100, message = "title must be between 1 and 100 characters")
  private String title;

  @NotNull(message = "description field is required")
  @Size(min = 1, max = 1000, message = "description must be between 1 and 1000 characters")
  private String description;

  @NotNull(message = "price field is required")
  @PositiveOrZero(message = "coinsPaid must be zero or positive")
  private BigDecimal price;

  @NotNull(message = "startDate is required")
  @FutureOrPresent(message = "startDate must be in the present or future")
  private LocalDateTime startDate;

  @NotNull(message = "endDate is required")
  @Future(message = "endDate must be in the future")
  private LocalDateTime endDate;

  @NotNull(message = "isPublic field is required")
  private Boolean isPublic;
}