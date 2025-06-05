package com.leverx.learningmanagementsystem.lesson;

import static com.leverx.learningmanagementsystem.util.ControllerResponseUtil.handleItemOrNotFound;
import static com.leverx.learningmanagementsystem.util.ControllerResponseUtil.handleList;

import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.UpdateLessonDto;
import com.leverx.learningmanagementsystem.lesson.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lessons", description = "Endpoints for managing lessons")
public class LessonController {

  private final LessonService lessonService;

  @Operation(summary = "Get a lesson by ID")
  @GetMapping("/{id}")
  public ResponseEntity<LessonDto> getLesson(
      @PathVariable @Parameter(description = "ID of the lesson to retrieve") UUID id) {
    log.info("Fetching lesson with ID: {}", id);
    return handleItemOrNotFound(lessonService.getLessonById(id));
  }

  @Operation(summary = "Get all lessons")
  @GetMapping
  public ResponseEntity<List<LessonDto>> getAllLessons() {
    log.info("Fetching all lessons");
    return handleList(lessonService.getAllLessons());
  }

  @Operation(summary = "Create a new lesson")
  @PostMapping
  public ResponseEntity<LessonDto> createLesson(
      @RequestBody @Valid @Parameter(description = "Lesson creation data") CreateLessonDto lessonDto) {
    log.info("Creating new lesson: {}", lessonDto);
    var newLesson = lessonService.createLesson(lessonDto);
    return ResponseEntity.ok(newLesson);
  }

  @Operation(summary = "Add an existing lesson to a course")
  @PostMapping("/{lessonId}/{courseId}")
  public ResponseEntity<Void> addToCourse(
      @PathVariable @Parameter(description = "Lesson ID to add") UUID lessonId,
      @PathVariable @Parameter(description = "Course ID to add lesson to") UUID courseId) {
    log.info("Adding lesson ID {} to course ID {}", lessonId, courseId);
    lessonService.addToCourse(courseId, lessonId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Delete a lesson by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLesson(
      @PathVariable @Parameter(description = "ID of the lesson to delete") UUID id) {
    log.warn("Deleting lesson with ID: {}", id);
    lessonService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update an existing lesson")
  @PutMapping("/{id}")
  public ResponseEntity<LessonDto> updateLesson(
      @PathVariable @Parameter(description = "Lesson id to update") UUID id,
      @RequestBody @Valid @Parameter(description = "Lesson update data") UpdateLessonDto lessonDtoDto) {
    log.info("Updating lesson: {}", lessonDtoDto);
    var updatedLesson = lessonService.updateLessons(id, lessonDtoDto);
    return ResponseEntity.ok(updatedLesson);
  }
}
