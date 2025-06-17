package com.leverx.learningmanagementsystem.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
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
public class ReplaceStudentDto implements StudentUpdateDto {

  @NotNull(message = "firstName is required")
  @Size(min = 1, max = 50, message = "first name must be between 1 and 50 characters")
  private String firstName;

  @NotNull(message = "lastName is required")
  @Size(min = 1, max = 50, message = "last name must be between 1 and 50 characters")
  private String lastName;

  @NotNull(message = "email is required")
  @Email(message = "email must be valid")
  private String email;

  @NotNull(message = "birthDate is required")
  @Past(message = "birthDate must be in the past")
  private LocalDate dateOfBirth;
}
