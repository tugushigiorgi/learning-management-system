package com.leverx.learning_management_system.course;

import static com.leverx.learning_management_system.ConstMessages.COURSE_ADDED;
import static com.leverx.learning_management_system.ConstMessages.EMAIL_SENT;
import static com.leverx.learning_management_system.util.ControllerResponse.handleItemOrNotFound;
import static com.leverx.learning_management_system.util.ControllerResponse.handleList;

import com.leverx.learning_management_system.course.Service.CourseService;
import com.leverx.learning_management_system.course.dto.CourseDto;
import com.leverx.learning_management_system.course.dto.CreateCourseDto;
import com.leverx.learning_management_system.course.dto.UpdateCourseDto;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Endpoints for managing courses")
public class CourseController {

  private final CourseService courseService;

  @Operation(summary = "Sends  mail to enrolled students")
  @PostMapping("/{courseId}/send-mail-to-students")
  public ResponseEntity<String> sendMailToEnrolledStudents(@PathVariable @Parameter(description = "Course Id") UUID courseId) {
    courseService.sendMailToEnrolledStudents(courseId);
    return ResponseEntity.ok(EMAIL_SENT);
  }

  @Operation(summary = "Get the most popular courses by coins paid")
  @GetMapping("/most-popular")
  public ResponseEntity<List<CourseDto>> MostPopularCourses() {
    return handleList(courseService.MostPopularCourses());
  }

  @Operation(summary = "Create a new course")
  @PostMapping
  public ResponseEntity<String> createCourse(@RequestBody @Valid @Parameter(description = "Course creation data") CreateCourseDto courseDto) {
    courseService.createCourse(courseDto);
    return ResponseEntity.ok(COURSE_ADDED);
  }

  @Operation(summary = "Get a course by ID")
  @GetMapping("/{id}")
  public ResponseEntity<CourseDto> getCourse(@PathVariable @Parameter(description = "ID of the course to retrieve") UUID id) {
    return handleItemOrNotFound(courseService.getCourseById(id));
  }

  @Operation(summary = "Get all courses")
  @GetMapping
  public ResponseEntity<List<CourseDto>> getAllCourses() {
    return handleList(courseService.getAllCourses());
  }

  @Operation(summary = "Delete a course by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCourse(@PathVariable @Parameter(description = "ID of the course to delete") UUID id) {
    courseService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update an existing course")
  @PutMapping
  public ResponseEntity<Void> updateCourse(@RequestBody @Valid @Parameter(description = "Course update data") UpdateCourseDto courseDtoDto) {
    courseService.updateCourse(courseDtoDto);
    return ResponseEntity.noContent().build();
  }
}
