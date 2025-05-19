package com.leverx.learning_management_system.course.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateCourseDto {
  private String title;
  private String description;
  private BigDecimal price;
}
