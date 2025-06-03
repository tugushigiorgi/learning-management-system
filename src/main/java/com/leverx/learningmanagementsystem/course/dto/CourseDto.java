package com.leverx.learningmanagementsystem.course.dto;

import java.math.BigDecimal;
import java.util.UUID;
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
public class CourseDto {
  private UUID id;
  private String title;
  private String description;
  private BigDecimal price;
  private BigDecimal coinsPaid;
}
