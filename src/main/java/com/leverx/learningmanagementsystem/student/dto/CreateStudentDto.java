package com.leverx.learningmanagementsystem.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateStudentDto {

  @NotBlank(message = "first name is required")
  @Size(min = 1, max = 50, message = "first name must be between 1 and 50 characters")
  private String firstName;

  @NotBlank(message = "last name is required")
  @Size(min = 1, max = 50, message = "last name must be between 1 and 50 characters")
  private String lastName;

  @NotBlank(message = "email is required")
  @Email(message = "email must be valid")
  private String email;

  @NotNull(message = "date of birth is required")
  @Past(message = "date of birth must be in the past")
  private LocalDate dateOfBirth;
}
