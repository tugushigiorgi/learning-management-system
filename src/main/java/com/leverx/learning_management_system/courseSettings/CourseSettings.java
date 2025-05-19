package com.leverx.learning_management_system.courseSettings;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
@Table(name = "courseSettings")
public class CourseSettings {
  @Id
  @GeneratedValue
  private UUID id;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Boolean isPublic;
}
