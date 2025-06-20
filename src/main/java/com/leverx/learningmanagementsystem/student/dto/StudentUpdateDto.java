package com.leverx.learningmanagementsystem.student.dto;

import java.time.LocalDate;

public interface StudentUpdateDto {

  String getFirstName();

  String getLastName();

  String getEmail();

  LocalDate getDateOfBirth();
}
