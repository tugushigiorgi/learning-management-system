package com.leverx.learning_management_system.lesson;

import static com.leverx.learning_management_system.ConstMessages.LESSON_ADDED;
import static com.leverx.learning_management_system.util.ControllerResponse.handleItemOrNotFound;
import static com.leverx.learning_management_system.util.ControllerResponse.handleList;

import com.leverx.learning_management_system.lesson.dto.CreateLessonDto;
import com.leverx.learning_management_system.lesson.dto.LessonDto;
import com.leverx.learning_management_system.lesson.dto.UpdateLessonDto;
import com.leverx.learning_management_system.lesson.service.LessonService;
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
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Tag(name = "Lessons", description = "Endpoints for managing lessons")
public class LessonController {

  private final LessonService lessonService;

  @Operation(summary = "Add an existing lesson to a course")
  @PostMapping("/{lessonId}/{courseId}")
  public ResponseEntity<Void> addToCourse(
      @PathVariable @Parameter(description = "Lesson ID to add") UUID lessonId,
      @PathVariable @Parameter(description = "Course ID to add lesson to") UUID courseId) {
    lessonService.addToCourse(courseId, lessonId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Create a new lesson")
  @PostMapping
  public ResponseEntity<String> createLesson(
      @RequestBody @Parameter(description = "Lesson creation data") CreateLessonDto lessonDto) {
    lessonService.createLesson(lessonDto);
    return ResponseEntity.ok(LESSON_ADDED);
  }

  @Operation(summary = "Get a lesson by ID")
  @GetMapping("/{id}")
  public ResponseEntity<LessonDto> getLesson(
      @PathVariable @Parameter(description = "ID of the lesson to retrieve") UUID id) {
    return handleItemOrNotFound(lessonService.getLessonById(id));
  }

  @Operation(summary = "Get all lessons")
  @GetMapping
  public ResponseEntity<List<LessonDto>> getAllLessons() {
    return handleList(lessonService.getAllLessons());
  }

  @Operation(summary = "Delete a lesson by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLesson(
      @PathVariable @Parameter(description = "ID of the lesson to delete") UUID id) {
    lessonService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update an existing lesson")
  @PutMapping
  public ResponseEntity<Void> updateLesson(
      @RequestBody @Parameter(description = "Lesson update data") UpdateLessonDto lessonDtoDto) {
    lessonService.updateLessons(lessonDtoDto);
    return ResponseEntity.noContent().build();
  }
}
