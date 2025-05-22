package com.leverx.learning_management_system;

import com.leverx.learning_management_system.course.service.CourseService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseScheduleJob {

  private final CourseService courseService;

  @Scheduled(cron = "0 0 0 * * *")
  public void collectCoursesStartingTomorrow() {
    var tomorrow = LocalDate.now().plusDays(1);
    courseService.printAllCoursesByStartDateBetween(tomorrow.atStartOfDay(), tomorrow.plusDays(1).atStartOfDay());
  }
}
