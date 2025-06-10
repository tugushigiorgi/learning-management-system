package com.leverx.learningmanagementsystem.lesson.service;

import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.UpdateLessonDto;
import java.util.List;
import java.util.UUID;

public interface LessonService {
  LessonDto createLesson(CreateLessonDto lessonDto);

  LessonDto getLessonById(UUID id);

  List<LessonDto> getAllLessons();

  void deleteById(UUID id);

  LessonDto updateLessons(UUID id, UpdateLessonDto lessonDto);

  void addToCourse(UUID courseId, UUID lessonId);
}
