package com.leverx.learning_management_system.course.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateCourseDto {
  private UUID id;
  private String title;
  private String description;
  private BigDecimal price;
  private BigDecimal coinsPaid;
}
