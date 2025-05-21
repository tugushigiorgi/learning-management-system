package com.leverx.learning_management_system.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentDto {
  @NotNull(message = "Student ID is required for update")
  private UUID id;

  @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
  private String firstName;

  @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
  private String lastName;

  @Email(message = "Email must be valid")
  private String email;

  @Past(message = "Date of birth must be in the past")
  private LocalDate dateOfBirth;
}
