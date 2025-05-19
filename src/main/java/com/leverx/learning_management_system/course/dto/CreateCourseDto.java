package com.leverx.learning_management_system.course.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateCourseDto {
  private String title;
  private String description;
  private BigDecimal price;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Boolean isPublic;
}
