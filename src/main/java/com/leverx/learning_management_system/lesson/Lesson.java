package com.leverx.learning_management_system.lesson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leverx.learning_management_system.course.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
public class Lesson {
  @Id
  @GeneratedValue
  private UUID id;
  private String title;
  private Integer duration;
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;
}