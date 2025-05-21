package com.leverx.learning_management_system;

import com.leverx.learning_management_system.course.Course;
import com.leverx.learning_management_system.course.CourseRepository;
import com.leverx.learning_management_system.course.Service.CourseService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseScheduleJob {

  private final CourseService courseService;

  @Scheduled(cron = "0 0 0 * * *")
  public void collectCoursesStartingTomorrow() {
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    courseService.printAlCourseByStartDateBetween(tomorrow.atStartOfDay(), tomorrow.plusDays(1).atStartOfDay());
  }
}
