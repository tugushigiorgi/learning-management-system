package com.leverx.learningmanagementsystem.lesson.service.impl;

import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learningmanagementsystem.ConstMessages.LESSON_ALREADY_ADDED;
import static com.leverx.learningmanagementsystem.ConstMessages.LESSON_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.learningmanagementsystem.course.CourseRepository;
import com.leverx.learningmanagementsystem.lesson.LessonRepository;
import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.UpdateLessonDto;
import com.leverx.learningmanagementsystem.lesson.service.LessonService;
import com.leverx.learningmanagementsystem.mapper.LessonMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
  private final LessonRepository lessonRepository;
  private final LessonMapper lessonMapper;
  private final CourseRepository courseRepository;

  @Override
  @Transactional
  public LessonDto createLesson(CreateLessonDto lessonDto) {
    var currentCourse = courseRepository.findById(lessonDto.getCourseId())
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, lessonDto.getCourseId())));
    var lesson = lessonMapper.toEntity(lessonDto);
    currentCourse.addLesson(lesson);
    lesson = lessonRepository.save(lesson);
    return lessonMapper.toDto(lesson);
  }

  @Override
  @Transactional(readOnly = true)
  public LessonDto getLessonById(UUID id) {
    return lessonRepository.findById(id)
        .map(lessonMapper::toDto)
        .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public List<LessonDto> getAllLessons() {
    return lessonRepository.findAll()
        .stream()
        .map(lessonMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    var lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(LESSON_NOT_FOUND, id)));
    lessonRepository.delete(lesson);
  }

  @Override
  @Transactional
  public LessonDto updateLessons(UUID id, UpdateLessonDto lessonDto) {
    var currentLesson = lessonRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(LESSON_NOT_FOUND, id)));
    lessonMapper.update(lessonDto, currentLesson);
    lessonRepository.save(currentLesson);
    return lessonMapper.toDto(currentLesson);
  }

  @Override
  @Transactional
  public void addToCourse(UUID courseId, UUID lessonId) {
    var currentLesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(LESSON_NOT_FOUND, lessonId)));
    var currentCourse = courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, courseId)));
    var alreadyAdded = currentCourse.getLessons()
        .stream()
        .anyMatch(lesson -> lesson.getId().equals(lessonId));
    if (alreadyAdded) {
      throw new ResponseStatusException(BAD_REQUEST, LESSON_ALREADY_ADDED);
    }
    currentCourse.getLessons().add(currentLesson);
    courseRepository.save(currentCourse);
  }
}
