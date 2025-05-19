package com.leverx.learning_management_system.student;

import static com.leverx.learning_management_system.ConstMessages.STUDENT_ADDED;
import static com.leverx.learning_management_system.ConstMessages.STUDENT_ENROLLED_SUCCESSFULLY;
import static com.leverx.learning_management_system.util.ControllerResponse.handleItemOrNotFound;
import static com.leverx.learning_management_system.util.ControllerResponse.handleList;

import com.leverx.learning_management_system.student.dto.CreateStudentDto;
import com.leverx.learning_management_system.student.dto.StudentDto;
import com.leverx.learning_management_system.student.dto.UpdateStudentDto;
import com.leverx.learning_management_system.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Students", description = "Endpoints for managing students and course enrollment")
public class StudentController {

  private final StudentService studentService;

  @Operation(summary = "Enroll a student in a course")
  @PostMapping("/enroll/{studentId}/{courseId}")
  public ResponseEntity<String> enrollToCourse(
      @PathVariable @Parameter(description = "ID of the student") UUID studentId,
      @PathVariable @Parameter(description = "ID of the course") UUID courseId) {
    studentService.enrollToCourse(studentId, courseId);
    return ResponseEntity.ok(STUDENT_ENROLLED_SUCCESSFULLY);
  }

  @Operation(summary = "Get the number of courses a student is enrolled in")
  @GetMapping("/{studentId}/enrolled-courses-count")
  public Integer getEnrolledCourseCount(
      @PathVariable @Parameter(description = "ID of the student") UUID studentId) {
    return studentService.getEnrolledCourseCount(studentId);
  }

  @Operation(summary = "Create a new student")
  @PostMapping
  public ResponseEntity<String> createStudent(
      @RequestBody @Parameter(description = "Student creation data") CreateStudentDto studentDto) {
    studentService.createStudent(studentDto);
    return ResponseEntity.ok(STUDENT_ADDED);
  }

  @Operation(summary = "Get a student by ID")
  @GetMapping("/{id}")
  public ResponseEntity<StudentDto> getStudent(
      @PathVariable @Parameter(description = "ID of the student to retrieve") UUID id) {
    return handleItemOrNotFound(studentService.getStudentById(id));
  }

  @Operation(summary = "Get all students")
  @GetMapping
  public ResponseEntity<List<StudentDto>> getAllStudents() {
    return handleList(studentService.getAllStudents());
  }

  @Operation(summary = "Delete a student by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStudent(
      @PathVariable @Parameter(description = "ID of the student to delete") UUID id) {
    studentService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update an existing student")
  @PutMapping
  public ResponseEntity<Void> updateStudent(
      @RequestBody @Parameter(description = "Student update data") UpdateStudentDto studentDto) {
    studentService.updateStudent(studentDto);
    return ResponseEntity.noContent().build();
  }
}
