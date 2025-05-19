package com.leverx.learning_management_system.course;

import static com.leverx.learning_management_system.ConstMessages.COURSE_ADDED;
import static com.leverx.learning_management_system.util.ControllerResponse.handleItemOrNotFound;
import static com.leverx.learning_management_system.util.ControllerResponse.handleList;

import com.leverx.learning_management_system.course.Service.CourseService;
import com.leverx.learning_management_system.course.dto.CourseDto;
import com.leverx.learning_management_system.course.dto.CreateCourseDto;
import com.leverx.learning_management_system.course.dto.UpdateCourseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CourseController {

  private final CourseService courseService;


  @GetMapping("/most-popular")
  public ResponseEntity<List<CourseDto>> MostPopularCourses() {
    return handleList(courseService.MostPopularCourses());
  }

  @PostMapping
  public ResponseEntity<String> createCourse(@RequestBody CreateCourseDto courseDto) {
    courseService.createCourse(courseDto);
    return ResponseEntity.ok(COURSE_ADDED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CourseDto> getCourse(@PathVariable UUID id) {
    return handleItemOrNotFound(courseService.getCourseById(id));
  }

  @GetMapping
  public ResponseEntity<List<CourseDto>> getAllCourses() {
    return handleList(courseService.getAllCourses());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
    courseService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateCourse(@RequestBody UpdateCourseDto courseDtoDto) {
    courseService.updateCourse(courseDtoDto);
    return ResponseEntity.noContent().build();
  }
}

