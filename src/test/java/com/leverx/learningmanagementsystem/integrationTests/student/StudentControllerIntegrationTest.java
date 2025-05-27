package com.leverx.learningmanagementsystem.integrationTests.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.student.StudentRepository;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class StudentControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private StudentRepository studentRepository;

  @Test
  void createStudent_shouldPersistAndReturnStudent() throws Exception {
    // Arrange.
    var dto = CreateStudentDto.builder()
        .firstName("Giorgi")
        .lastName("Tughushi")
        .email("giorgi@gmail.com")
        .dateOfBirth(LocalDate.of(2001, 1, 1))
        .build();
    var json = objectMapper.writeValueAsString(dto);

    // Act & Assert: send POST request and verify response JSON structure and values
    mockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Giorgi"))
        .andExpect(jsonPath("$.lastName").value("Tughushi"))
        .andExpect(jsonPath("$.email").value("giorgi@gmail.com"))
        .andExpect(jsonPath("$.dateOfBirth").value("2001-01-01"));

    // Assert: verify student was persisted in the database with correct values
    var savedStudents = studentRepository.findAll();
    assertThat(savedStudents).hasSize(1);
    var saved = savedStudents.get(0);
    assertThat(saved.getFirstName()).isEqualTo("Giorgi");
    assertThat(saved.getLastName()).isEqualTo("Tughushi");
    assertThat(saved.getEmail()).isEqualTo("giorgi@gmail.com");
    assertThat(saved.getDateOfBirth()).isEqualTo(LocalDate.of(2001, 1, 1));
  }
}
