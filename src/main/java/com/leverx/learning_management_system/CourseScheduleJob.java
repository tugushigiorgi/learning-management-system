package com.leverx.learning_management_system;

import com.leverx.learning_management_system.course.Course;
import com.leverx.learning_management_system.course.CourseRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseScheduleJob {

  private final CourseRepository courseRepository;

  @Scheduled(cron = "0 0 0 * * *")
  public void collectCoursesStartingTomorrow() {
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    List<Course> courses = courseRepository.findAllByStartDateBetween(
        tomorrow.atStartOfDay(),
        tomorrow.plusDays(1).atStartOfDay()
    );
    System.out.println("Courses starting tomorrow: " + courses.size());
    courses.forEach(c -> System.out.println("- " + c.getTitle()));
  }
}
