package com.leverx.learningmanagementsystem.course.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CourseUpdateDto {

  String getTitle();

  String getDescription();

  BigDecimal getPrice();

  BigDecimal getCoinsPaid();

  LocalDateTime getStartDate();

  LocalDateTime getEndDate();

  Boolean getIsPublic();
}
