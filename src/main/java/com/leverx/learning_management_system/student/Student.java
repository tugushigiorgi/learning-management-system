package com.leverx.learning_management_system.student;

import com.leverx.learning_management_system.course.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class Student {
  @Id
  @GeneratedValue
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private LocalDate dateOfBirth;
  private BigDecimal coins;
  @ManyToMany
  @JoinTable(
      name = "student_courses",
      joinColumns = @JoinColumn(name = "studentId"),
      inverseJoinColumns = @JoinColumn(name = "courseId")
  )
  private Set<Course> courses;
}
