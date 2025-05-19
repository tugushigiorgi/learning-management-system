package com.leverx.learning_management_system.student;

import static com.leverx.learning_management_system.ConstMessages.STUDENT_ADDED;
import static com.leverx.learning_management_system.util.ControllerResponse.handleItemOrNotFound;
import static com.leverx.learning_management_system.util.ControllerResponse.handleList;

import com.leverx.learning_management_system.student.dto.CreateStudentDto;
import com.leverx.learning_management_system.student.dto.StudentDto;
import com.leverx.learning_management_system.student.dto.UpdateStudentDto;
import com.leverx.learning_management_system.student.service.StudentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

  private final StudentService studentService;

  @PostMapping
  public ResponseEntity<String> createStudent(@RequestBody CreateStudentDto studentDto) {
    studentService.createStudent(studentDto);
    return ResponseEntity.ok(STUDENT_ADDED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<StudentDto> getStudent(@PathVariable UUID id) {
    return handleItemOrNotFound(studentService.getStudentById(id));
  }

  @GetMapping
  public ResponseEntity<List<StudentDto>> getAllStudents() {
    return handleList(studentService.getAllStudents());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
    studentService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateStudent(@RequestBody UpdateStudentDto studentDto) {
    studentService.updateStudent(studentDto);
    return ResponseEntity.noContent().build();
  }
}

