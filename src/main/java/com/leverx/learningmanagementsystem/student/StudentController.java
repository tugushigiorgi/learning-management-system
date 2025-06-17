package com.leverx.learningmanagementsystem.student;

import static com.leverx.learningmanagementsystem.ConstMessages.STUDENT_ENROLLED_SUCCESSFULLY;
import static com.leverx.learningmanagementsystem.util.ControllerResponseUtil.handleItemOrNotFound;
import static com.leverx.learningmanagementsystem.util.ControllerResponseUtil.handleList;

import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentDto;
import com.leverx.learningmanagementsystem.student.dto.UpdateStudentDto;
import com.leverx.learningmanagementsystem.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Students", description = "Endpoints for managing students and course enrollment")
public class StudentController {

  private final StudentService studentService;

  @Operation(summary = "Enroll a student in a course")
  @PostMapping("/enroll/{studentId}/{courseId}")
  @Profile("prod")
  public ResponseEntity<String> enrollToCourse(
      @PathVariable @Parameter(description = "ID of the student") UUID studentId,
      @PathVariable @Parameter(description = "ID of the course") UUID courseId) throws MessagingException {
    log.info("Enrolling student {} to course {}", studentId, courseId);
    studentService.enrollToCourse(studentId, courseId);
    return ResponseEntity.ok(STUDENT_ENROLLED_SUCCESSFULLY);
  }

  @Operation(summary = "Get the number of courses a student is enrolled in")
  @GetMapping("/{studentId}/enrolled-courses-count")
  public Integer getEnrolledCourseCount(
      @PathVariable @Parameter(description = "ID of the student") UUID studentId) {
    log.info("Retrieving enrolled course count for student {}", studentId);
    return studentService.getEnrolledCourseCount(studentId);
  }

  @Operation(summary = "Create a new student")
  @PostMapping
  public ResponseEntity<StudentDto> createStudent(
      @RequestBody @Valid @Parameter(description = "Student creation data") CreateStudentDto studentDto) {
    log.info("Creating new student: {}", studentDto);
    var newStudent = studentService.createStudent(studentDto);
    return ResponseEntity.ok(newStudent);
  }

  @Operation(summary = "Get a student by ID")
  @GetMapping("/{id}")
  public ResponseEntity<StudentDto> getStudent(
      @PathVariable @Parameter(description = "ID of the student to retrieve") UUID id) {
    log.info("Fetching student with ID: {}", id);
    return handleItemOrNotFound(studentService.getStudentById(id));
  }

  @Operation(summary = "Get all students")
  @GetMapping
  public ResponseEntity<List<StudentDto>> getAllStudents() {
    log.info("Fetching all students");
    return handleList(studentService.getAllStudents());
  }

  @Operation(summary = "Delete a student by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStudent(
      @PathVariable @Parameter(description = "ID of the student to delete") UUID id) {
    log.warn("Deleting student with ID: {}", id);
    studentService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update an existing student")
  @PutMapping("/{id}")
  public ResponseEntity<StudentDto> updateStudent(
      @PathVariable @Parameter(description = "Student id to update") UUID id,
      @RequestBody @Valid @Parameter(description = "Student update data") UpdateStudentDto studentDto) {
    log.info("Updating student: {}", studentDto);
    var updatedStudent = studentService.updateStudent(id, studentDto);
    return ResponseEntity.ok(updatedStudent);
  }
}