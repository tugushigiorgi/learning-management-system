package com.leverx.learning_management_system.lesson;

import static com.leverx.learning_management_system.ConstMessages.LESSON_ADDED;
import static com.leverx.learning_management_system.util.ControllerResponse.handleItemOrNotFound;
import static com.leverx.learning_management_system.util.ControllerResponse.handleList;

import com.leverx.learning_management_system.lesson.dto.CreateLessonDto;
import com.leverx.learning_management_system.lesson.dto.LessonDto;
import com.leverx.learning_management_system.lesson.dto.UpdateLessonDto;
import com.leverx.learning_management_system.lesson.service.LessonService;
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
public class LessonController {
  private final LessonService lessonService;

  @PostMapping
  public ResponseEntity<String> createLesson(@RequestBody CreateLessonDto lessonDto) {
    lessonService.createLesson(lessonDto);
    return ResponseEntity.ok(LESSON_ADDED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LessonDto> getLesson(@PathVariable UUID id) {
    return handleItemOrNotFound(lessonService.getLessonById(id));
  }

  @GetMapping
  public ResponseEntity<List<LessonDto>> getAllLessons() {
    return handleList(lessonService.getAllLessons());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLesson(@PathVariable UUID id) {
    lessonService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateLesson(@RequestBody UpdateLessonDto lessonDtoDto) {
    lessonService.updateLessons(lessonDtoDto);
    return ResponseEntity.noContent().build();
  }
}
