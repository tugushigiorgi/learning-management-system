package com.leverx.learningmanagementsystem.integrationTests.course;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leverx.learningmanagementsystem.course.Course;
import com.leverx.learningmanagementsystem.course.CourseRepository;
import com.leverx.learningmanagementsystem.courseSettings.CourseSettings;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CourseRepository courseRepository;

  @Test
  @Transactional
  void getAllCourses_shouldReturnAllCourses() throws Exception {
    // Arrange: create and persist two Course entities with different properties
    var course1 = Course.builder()
        .title("Java Basics")
        .description("Intro to Java")
        .price(BigDecimal.valueOf(100))
        .settings(CourseSettings.builder()
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(30))
            .isPublic(true)
            .build())
        .build();

    var course2 = Course.builder()
        .title("Spring Boot")
        .description("Spring Boot Deep Dive")
        .price(BigDecimal.valueOf(100))
        .settings(CourseSettings.builder()
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(60))
            .isPublic(false)
            .build())
        .build();

    courseRepository.saveAll(List.of(course1, course2));

    // Act & Assert: perform GET request and verify response contains both courses
    mockMvc.perform(get("/api/courses")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].title").value("Java Basics"))
        .andExpect(jsonPath("$[1].title").value("Spring Boot"));
  }
}
