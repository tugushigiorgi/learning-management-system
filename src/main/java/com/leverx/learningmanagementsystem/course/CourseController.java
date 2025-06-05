package com.leverx.learningmanagementsystem.course;

import static com.leverx.learningmanagementsystem.ConstMessages.EMAIL_SENT;
import static com.leverx.learningmanagementsystem.util.ControllerResponseUtil.handleItemOrNotFound;
import static com.leverx.learningmanagementsystem.util.ControllerResponseUtil.handleList;

import com.leverx.learningmanagementsystem.course.dto.CourseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.dto.UpdateCourseDto;
import com.leverx.learningmanagementsystem.course.service.CourseService;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
@Tag(name = "Courses", description = "Endpoints for managing courses")
public class CourseController {

  private final CourseService courseService;

  @Operation(summary = "Get a course by ID")
  @GetMapping("/{id}")
  public ResponseEntity<CourseDto> getCourse(@PathVariable @Parameter(description = "ID of the course to retrieve") UUID id) {
    log.info("Fetching course with ID: {}", id);
    return handleItemOrNotFound(courseService.getCourseById(id));
  }

  @Operation(summary = "Get the most popular courses by coins paid")
  @GetMapping("/most-popular")
  public ResponseEntity<List<CourseDto>> getMostPopularCourses() {
    log.info("Fetching most popular courses");
    return handleList(courseService.getMostPopularCourses());
  }

  @Operation(summary = "Get all courses")
  @GetMapping
  public ResponseEntity<List<CourseDto>> getAllCourses() {
    log.info("Fetching all courses");
    return handleList(courseService.getAllCourses());
  }

  @Operation(summary = "Create a new course")
  @PostMapping
  public ResponseEntity<CourseDto> createCourse(@RequestBody @Valid @Parameter(description = "Course creation data") CreateCourseDto courseDto) {
    log.info("Creating new course: {}", courseDto);
    var newCourse = courseService.createCourse(courseDto);
    return ResponseEntity.ok(newCourse);
  }

  @Operation(summary = "Sends  mail to enrolled students")
  @PostMapping("/{courseId}/send-mail-to-students")
  public ResponseEntity<String> sendMailToEnrolledStudents(@PathVariable @Parameter(description = "Course Id") UUID courseId) throws MessagingException {
    log.info("Sending mail to enrolled students for course ID: {}", courseId);
    courseService.sendMailToEnrolledStudents(courseId);
    return ResponseEntity.ok(EMAIL_SENT);
  }

  @Operation(summary = "Delete a course by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCourse(@PathVariable @Parameter(description = "ID of the course to delete") UUID id) {
    log.warn("Deleting course with ID: {}", id);
    courseService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update an existing course")
  @PutMapping("/{id}")
  public ResponseEntity<CourseDto> updateCourse(@PathVariable @Parameter(description = "Course id to update") UUID id, @RequestBody @Valid @Parameter(description = "Course update data") UpdateCourseDto courseDtoDto) {
    log.info("Updating course: {}", courseDtoDto);
    var updatedCourse = courseService.updateCourse(id, courseDtoDto);
    return ResponseEntity.ok(updatedCourse);
  }
}

