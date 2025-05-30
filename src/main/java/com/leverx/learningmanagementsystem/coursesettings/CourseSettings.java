package com.leverx.learningmanagementsystem.coursesettings;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course_settings")
public class CourseSettings {
  @Id
  @GeneratedValue
  private UUID id;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Boolean isPublic;
}
