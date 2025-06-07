package com.leverx.learningmanagementsystem;

import com.leverx.learningmanagementsystem.course.service.CourseService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class CourseScheduleJob {

  private final CourseService courseService;

  @Scheduled(cron = "0 0 0 * * *")
  public void collectCoursesStartingTomorrow() {
    var tomorrow = LocalDate.now().plusDays(1);
    courseService.printAllCoursesByStartDateBetween(tomorrow.atStartOfDay(), tomorrow.plusDays(1).atStartOfDay());
  }
}
