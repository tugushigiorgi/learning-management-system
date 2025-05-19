package com.leverx.learning_management_system.course.dto;

import java.math.BigDecimal;
import lombok.Data;


@Data
public class CourseDto {
  private String title;
  private String description;
  private BigDecimal price;
  private BigDecimal coinsPaid;
}
