package com.leverx.learning_management_system.student.dto;

import java.time.LocalDate;
import lombok.Data;


@Data
public class CreateStudentDto {
  private String firstName;
  private String lastName;
  private String email;
  private LocalDate dateOfBirth;
}
