package com.leverx.learningmanagementsystem.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leverx.learningmanagementsystem.coursesettings.CourseSettings;
import com.leverx.learningmanagementsystem.lesson.Lesson;
import com.leverx.learningmanagementsystem.student.Student;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courses")
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String title;
  private String description;
  private BigDecimal price;
  private BigDecimal coinsPaid;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "course_settings_id", referencedColumnName = "id")
  private CourseSettings settings;

  @JsonIgnore
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  private Set<Lesson> lessons = new HashSet<>();

  @JsonIgnore
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToMany(mappedBy = "courses")
  private Set<Student> students = new HashSet<>();

  public void addLesson(Lesson lesson) {
    lessons.add(lesson);
    lesson.setCourse(this);
  }
}