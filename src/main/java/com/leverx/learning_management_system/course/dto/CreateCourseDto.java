package com.leverx.learning_management_system.course.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateCourseDto {
  private String title;
  private String description;
  private BigDecimal price;
}
