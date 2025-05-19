package com.leverx.learning_management_system;

import com.leverx.learning_management_system.student.Student;
import com.leverx.learning_management_system.student.StudentRepository;
import com.leverx.learning_management_system.student.service.StudentService;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;

public class Runner implements CommandLineRunner {

  private StudentRepository studentRepository;
  private StudentService studentService;

  @Override
  public void run(String... args) throws Exception {
    var c = new Student(

        UUID.randomUUID(), "Giorgi", "Tughushi", "giorgi@gmail.com"
        , LocalDate.now(), BigDecimal.ONE, Set.of()


    );

    studentRepository.save(c);
    System.out.println("ID"+c.getId());
  }
}
