package com.leverx.learning_management_system.student.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;


@Data
public class UpdateStudentDto {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private LocalDate dateOfBirth;
}
