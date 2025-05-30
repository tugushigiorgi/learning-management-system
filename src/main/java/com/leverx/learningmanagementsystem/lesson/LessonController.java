package com.leverx.learningmanagementsystem.lesson;

import static com.leverx.learningmanagementsystem.util.ControllerResponse.handleItemOrNotFound;
import static com.leverx.learningmanagementsystem.util.ControllerResponse.handleList;

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

  @Operation(summary = "Create a new lesson")
  @PostMapping
  public ResponseEntity<LessonDto> createLesson(
      @RequestBody @Valid  @Parameter(description = "Lesson creation data") CreateLessonDto lessonDto) {
    var newLesson=lessonService.createLesson(lessonDto);
    return ResponseEntity.ok(newLesson);
  }

  @Operation(summary = "Add an existing lesson to a course")
  @PostMapping("/{lessonId}/{courseId}")
  public ResponseEntity<Void> addToCourse(
      @PathVariable @Parameter(description = "Lesson ID to add") UUID lessonId,
      @PathVariable @Parameter(description = "Course ID to add lesson to") UUID courseId) {
    lessonService.addToCourse(courseId, lessonId);
    return ResponseEntity.ok().build();
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
  public ResponseEntity<LessonDto> updateLesson(
      @RequestBody @Valid @Parameter(description = "Lesson update data") UpdateLessonDto lessonDtoDto) {
    var updatedLesson=lessonService.updateLessons(lessonDtoDto);
    return ResponseEntity.ok(updatedLesson);
  }
}
