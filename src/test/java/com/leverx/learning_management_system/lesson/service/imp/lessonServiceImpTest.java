package com.leverx.learning_management_system.lesson.service.imp;

import static com.leverx.learning_management_system.ConstMessages.LESSON_ALREADY_ADDED;
import static com.leverx.learning_management_system.ConstMessages.LESSON_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.learning_management_system.Mapper.LessonMapper;
import com.leverx.learning_management_system.course.Course;
import com.leverx.learning_management_system.course.CourseRepository;
import com.leverx.learning_management_system.lesson.Lesson;
import com.leverx.learning_management_system.lesson.LessonRepository;
import com.leverx.learning_management_system.lesson.dto.CreateLessonDto;
import com.leverx.learning_management_system.lesson.dto.LessonDto;
import com.leverx.learning_management_system.lesson.dto.UpdateLessonDto;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class lessonServiceImpTest {

  private LessonRepository lessonRepository;
  private LessonMapper lessonMapper;
  private CourseRepository courseRepository;
  private lessonServiceImp lessonService;

  @BeforeEach
  void setUp() {
    lessonRepository = mock(LessonRepository.class);
    lessonMapper = mock(LessonMapper.class);
    courseRepository = mock(CourseRepository.class);
    lessonService = new lessonServiceImp(lessonRepository, lessonMapper, courseRepository);
  }

  @Test
  void createLesson_shouldSaveLesson() {
    var dto = CreateLessonDto.builder()
        .title("Intro to Java programming")
        .duration(11)
        .build();
    lessonService.createLesson(dto);
    verify(lessonRepository).save(any(Lesson.class));
  }

  @Test
  void getLessonById_shouldReturnLessonDtoIfFound() {
    var id = UUID.randomUUID();
    var lesson = Lesson.builder()
        .id(id)
        .title("Intro to Java programming")
        .duration(45)
        .build();
    var dto = LessonDto.builder()
        .title("Intro to Java programming")
        .duration(45)
        .build();
    when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
    when(lessonMapper.toDto(lesson)).thenReturn(dto);
    var result = lessonService.getLessonById(id);
    assertEquals(dto, result);
  }

  @Test
  void getLessonById_shouldReturnNullIfNotFound() {
    var id = UUID.randomUUID();
    when(lessonRepository.findById(id)).thenReturn(Optional.empty());
    assertNull(lessonService.getLessonById(id));
  }

  @Test
  void getAllLessons_shouldReturnAllLessons() {
    var lessons = List.of(Lesson.builder()
        .id(UUID.randomUUID())
        .title("Intro to Java programming")
        .duration(30)
        .build());
    var dtos = List.of(LessonDto.builder()
        .title("Intro to Java programming")
        .duration(30)
        .build());
    when(lessonRepository.findAll()).thenReturn(lessons);
    when(lessonMapper.toDto(lessons.get(0))).thenReturn(dtos.get(0));
    List<LessonDto> result = lessonService.getAllLessons();
    assertEquals(dtos, result);
  }

  @Test
  void deleteById_shouldDeleteIfFound() {
    var id = UUID.randomUUID();
    var lesson = Lesson.builder()
        .id(id)
        .build();
    when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
    lessonService.deleteById(id);
    verify(lessonRepository).delete(lesson);
  }

  @Test
  void deleteById_shouldThrowIfNotFound() {
    var id = UUID.randomUUID();
    when(lessonRepository.findById(id)).thenReturn(Optional.empty());
    var ex = assertThrows(ResponseStatusException.class,
        () -> lessonService.deleteById(id));
    assertEquals(NOT_FOUND, ex.getStatusCode());
    assertTrue(ex.getReason().contains(LESSON_NOT_FOUND));
  }

  @Test
  void updateLessons_shouldUpdateLesson() {
    var id = UUID.randomUUID();
    var lesson = Lesson.builder()
        .id(id)
        .title("Java 8 programming course")
        .duration(30)
        .build();
    var dto = UpdateLessonDto.builder()
        .id(id)
        .title("Java 21 programming course")
        .duration(60)
        .build();
    when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
    lessonService.updateLessons(dto);
    assertEquals("Java 21 programming course", lesson.getTitle());
    assertEquals(60, lesson.getDuration());
    verify(lessonRepository).save(lesson);
  }

  @Test
  void updateLessons_shouldThrowIfNotFound() {
    var id = UUID.randomUUID();
    var dto = UpdateLessonDto.builder()
        .id(id)
        .title("programming course")
        .duration(12)
        .build();
    when(lessonRepository.findById(id)).thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class,
        () -> lessonService.updateLessons(dto));
  }

  @Test
  void addToCourse_shouldAddLessonToCourse() {
    var courseId = UUID.randomUUID();
    var lessonId = UUID.randomUUID();
    var lesson = Lesson.builder()
        .id(lessonId)
        .title("Java 21 programming course")
        .duration(60)
        .build();
    var course = Course.builder()
        .id(courseId)
        .lessons(new HashSet<>())
        .build();
    when(lessonRepository.findById(lessonId))
        .thenReturn(Optional.of(lesson));
    when(courseRepository.findById(courseId))
        .thenReturn(Optional.of(course));
    lessonService.addToCourse(courseId, lessonId);
    assertTrue(course.getLessons().contains(lesson));
    verify(courseRepository).save(course);
  }

  @Test
  void addToCourse_shouldThrowIfLessonAlreadyAdded() {
    var courseId = UUID.randomUUID();
    var lessonId = UUID.randomUUID();
    var lesson = Lesson.builder()
        .title("Python programming course")
        .id(lessonId)
        .build();
    var lessons = new HashSet<Lesson>();
    lessons.add(lesson);
    var course = Course
        .builder()
        .id(courseId)
        .lessons(lessons)
        .build();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> lessonService.addToCourse(courseId, lessonId));
    assertEquals(BAD_REQUEST, ex.getStatusCode());
    assertEquals(LESSON_ALREADY_ADDED, ex.getReason());
  }

  @Test
  void addToCourse_shouldThrowIfLessonNotFound() {
    var courseId = UUID.randomUUID();
    var lessonId = UUID.randomUUID();
    when(lessonRepository.findById(lessonId))
        .thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class,
        () -> lessonService.addToCourse(courseId, lessonId));

  }

  @Test
  void addToCourse_shouldThrowIfCourseNotFound() {
    var courseId = UUID.randomUUID();
    var lessonId = UUID.randomUUID();
    var lesson = Lesson.builder()
        .id(lessonId)
        .build();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class,
        () -> lessonService.addToCourse(courseId, lessonId));
  }
}