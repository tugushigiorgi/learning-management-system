package com.leverx.learningmanagementsystem.integrationTests.lesson;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leverx.learningmanagementsystem.lesson.Lesson;
import com.leverx.learningmanagementsystem.lesson.LessonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private LessonRepository lessonRepository;

  @Test
  @Transactional
  void getLessonById_shouldReturnLessonDto() throws Exception {
    // Arrange: create and save a Lesson entity in the database
    var lesson = Lesson.builder()
        .title("Spring Boot Basics")
        .duration(45)
        .build();
    lesson = lessonRepository.save(lesson);

    // Act & Assert: perform GET request and verify response content
    mockMvc.perform(get("/api/lessons/" + lesson.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Spring Boot Basics"))
        .andExpect(jsonPath("$.duration").value(45));
  }
}

