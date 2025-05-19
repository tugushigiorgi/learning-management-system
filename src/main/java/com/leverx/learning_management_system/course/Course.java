package com.leverx.learning_management_system.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leverx.learning_management_system.courseSettings.CourseSettings;
import com.leverx.learning_management_system.lesson.Lesson;
import com.leverx.learning_management_system.student.Student;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.Set;
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
public class Course {
  @Id
  @GeneratedValue
  private UUID id;
  private String title;
  private String description;
  private BigDecimal price;
  private BigDecimal coinsPaid;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "course_settings_id", referencedColumnName = "id")
  private CourseSettings settings;
  @JsonIgnore
  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Lesson> lessons;
  @JsonIgnore
  @ManyToMany(mappedBy = "courses")
  private Set<Student> students;
}