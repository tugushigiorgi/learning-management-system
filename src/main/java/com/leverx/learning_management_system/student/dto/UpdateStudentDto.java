package com.leverx.learning_management_system.student.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentDto {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private LocalDate dateOfBirth;
}
