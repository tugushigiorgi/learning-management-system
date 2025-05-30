package com.leverx.learningmanagementsystem.student;

import static com.leverx.learningmanagementsystem.ConstMessages.STUDENT_ENROLLED_SUCCESSFULLY;
import static com.leverx.learningmanagementsystem.util.ControllerResponse.handleItemOrNotFound;
import static com.leverx.learningmanagementsystem.util.ControllerResponse.handleList;

import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentDto;
import com.leverx.learningmanagementsystem.student.dto.UpdateStudentDto;
import com.leverx.learningmanagementsystem.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
  public ResponseEntity<StudentDto> createStudent(
      @RequestBody @Valid @Parameter(description = "Student creation data") CreateStudentDto studentDto) {
    var newStudent=studentService.createStudent(studentDto);
    return ResponseEntity.ok(newStudent);
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
  public ResponseEntity<StudentDto> updateStudent(
      @RequestBody @Valid @Parameter(description = "Student update data") UpdateStudentDto studentDto) {
    var updatedStudent=studentService.updateStudent(studentDto);
    return ResponseEntity.ok(updatedStudent);
  }
}
