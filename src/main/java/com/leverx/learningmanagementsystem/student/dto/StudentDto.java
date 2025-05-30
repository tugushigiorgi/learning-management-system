package com.leverx.learningmanagementsystem.student.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentDto {
  private String firstName;
  private String lastName;
  private String email;
  private LocalDate dateOfBirth;
  private BigDecimal coins;
}
