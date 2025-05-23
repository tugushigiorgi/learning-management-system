package com.leverx.learning_management_system.lesson.service.imp;

import static com.leverx.learning_management_system.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learning_management_system.ConstMessages.LESSON_ALREADY_ADDED;
import static com.leverx.learning_management_system.ConstMessages.LESSON_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.learning_management_system.course.CourseRepository;
import com.leverx.learning_management_system.lesson.Lesson;
import com.leverx.learning_management_system.lesson.LessonRepository;
import com.leverx.learning_management_system.lesson.dto.CreateLessonDto;
import com.leverx.learning_management_system.lesson.dto.LessonDto;
import com.leverx.learning_management_system.lesson.dto.UpdateLessonDto;
import com.leverx.learning_management_system.lesson.service.LessonService;
import com.leverx.learning_management_system.mapper.LessonMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LessonServiceImp implements LessonService {
  private final LessonRepository lessonRepository;
  private final LessonMapper lessonMapper;
  private final CourseRepository courseRepository;

  @Override
  @Transactional
  public LessonDto createLesson(CreateLessonDto lessonDto) {
    var newLesson = Lesson.builder()
        .title(lessonDto.getTitle())
        .duration(lessonDto.getDuration())
        .build();
    var savedLesson =lessonRepository.save(newLesson);
    return lessonMapper.toDto(savedLesson);
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
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, String.format(LESSON_NOT_FOUND, id)));
    lessonRepository.delete(lesson);
  }

  @Override
  @Transactional
  public LessonDto updateLessons(UpdateLessonDto lessonDto) {
    var currentLesson = lessonRepository.findById(lessonDto.getId())
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, String.format(LESSON_NOT_FOUND, lessonDto.getId())));
    if (!lessonDto.getTitle().equals(currentLesson.getTitle())) {
      currentLesson.setTitle(lessonDto.getTitle());
    }
    if (!lessonDto.getDuration().equals(currentLesson.getDuration())) {
      currentLesson.setDuration(lessonDto.getDuration());
    }
    var savedLesson=lessonRepository.save(currentLesson);
    return lessonMapper.toDto(savedLesson);
  }

  @Override
  @Transactional
  public void addToCourse(UUID courseId, UUID lessonId) {
    var currentLesson = lessonRepository.findById(lessonId)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, String.format(LESSON_NOT_FOUND, lessonId)));
    var currentCourse = courseRepository.findById(courseId)
        .orElseThrow(() ->
        new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, courseId)));
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
