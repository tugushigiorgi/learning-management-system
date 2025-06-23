package com.leverx.learningmanagementsystem.student.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
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
public class PatchStudentDto implements StudentUpdateDto {

  @Nullable
  @Size(min = 1, max = 50, message = "first name must be between 1 and 50 characters")
  private String firstName;

  @Nullable
  @Size(min = 1, max = 50, message = "last name must be between 1 and 50 characters")
  private String lastName;

  @Nullable
  @Email(message = "email must be valid")
  private String email;

  @Nullable
  @Past(message = "date of birth must be in the past")
  private LocalDate dateOfBirth;
}
