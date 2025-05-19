package com.leverx.learning_management_system.lesson.service;

import com.leverx.learning_management_system.lesson.dto.CreateLessonDto;
import com.leverx.learning_management_system.lesson.dto.LessonDto;
import com.leverx.learning_management_system.lesson.dto.UpdateLessonDto;
import java.util.List;
import java.util.UUID;

public interface LessonService {
  void createLesson(CreateLessonDto lessonDto);

  LessonDto getLessonById(UUID id);

  List<LessonDto> getAllLessons();

  void deleteById(UUID id);

  void updateLessons(UpdateLessonDto lessonDto);
}
