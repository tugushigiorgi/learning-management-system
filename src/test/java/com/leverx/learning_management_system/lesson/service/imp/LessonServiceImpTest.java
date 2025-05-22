package com.leverx.learning_management_system.lesson.service.imp;

import static com.leverx.learning_management_system.ConstMessages.LESSON_ALREADY_ADDED;
import static com.leverx.learning_management_system.ConstMessages.LESSON_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.learning_management_system.course.Course;
import com.leverx.learning_management_system.course.CourseRepository;
import com.leverx.learning_management_system.lesson.Lesson;
import com.leverx.learning_management_system.lesson.LessonRepository;
import com.leverx.learning_management_system.lesson.dto.CreateLessonDto;
import com.leverx.learning_management_system.lesson.dto.LessonDto;
import com.leverx.learning_management_system.lesson.dto.UpdateLessonDto;
import com.leverx.learning_management_system.mapper.LessonMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class LessonServiceImpTest {

  @Mock
  private LessonRepository lessonRepository;

  @Mock
  private LessonMapper lessonMapper;

  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private LessonServiceImp lessonService;

  private Lesson lesson;
  private UUID lessonId;
  private UUID courseId;

  @BeforeEach
  void setUp() {
    lessonId = UUID.randomUUID();
    courseId = UUID.randomUUID();
    lesson = Lesson.builder()
        .id(lessonId)
        .title("test")
        .duration(30)
        .build();
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
    var dto = LessonDto.builder()
        .title("test")
        .duration(30)
        .build();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
    when(lessonMapper.toDto(lesson)).thenReturn(dto);
    var result = lessonService.getLessonById(lessonId);
    assertEquals(dto, result);
  }

  @Test
  void getLessonById_shouldReturnNullIfNotFound() {
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
    assertNull(lessonService.getLessonById(lessonId));
  }

  @Test
  void getAllLessons_shouldReturnAllLessons() {
    var lessons = List.of(lesson);
    var dtos = List.of(LessonDto.builder()
        .title("test")
        .duration(30)
        .build());
    when(lessonRepository.findAll()).thenReturn(lessons);
    when(lessonMapper.toDto(lessons.getFirst())).thenReturn(dtos.getFirst());
    List<LessonDto> result = lessonService.getAllLessons();
    assertEquals(dtos, result);
  }

  @Test
  void deleteById_shouldDeleteIfFound() {
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
    lessonService.deleteById(lessonId);
    verify(lessonRepository).delete(lesson);
  }

  @Test
  void deleteById_shouldThrowIfNotFound() {
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
    var ex = assertThrows(ResponseStatusException.class,
        () -> lessonService.deleteById(lessonId));
    assertEquals(NOT_FOUND, ex.getStatusCode());
    assertTrue(ex.getReason().contains(LESSON_NOT_FOUND));
  }

  @Test
  void updateLessons_shouldUpdateLesson() {
    var dto = UpdateLessonDto.builder()
        .id(lessonId)
        .title("test")
        .duration(30)
        .build();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
    lessonService.updateLessons(dto);
    assertEquals("test", lesson.getTitle());
    assertEquals(30, lesson.getDuration());
    verify(lessonRepository).save(lesson);
  }

  @Test
  void updateLessons_shouldThrowIfNotFound() {
    var dto = UpdateLessonDto.builder()
        .id(lessonId)
        .title("programming course")
        .duration(12)
        .build();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class,
        () -> lessonService.updateLessons(dto));
  }

  @Test
  void addToCourse_shouldAddLessonToCourse() {
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
    var lessons = new HashSet<Lesson>();
    lessons.add(lesson);
    var course = Course
        .builder()
        .id(courseId)
        .lessons(lessons)
        .build();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    var ex = assertThrows(ResponseStatusException.class,
        () -> lessonService.addToCourse(courseId, lessonId));
    assertEquals(BAD_REQUEST, ex.getStatusCode());
    assertEquals(LESSON_ALREADY_ADDED, ex.getReason());
  }

  @Test
  void addToCourse_shouldThrowIfLessonNotFound() {
    when(lessonRepository.findById(lessonId))
        .thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class,
        () -> lessonService.addToCourse(courseId, lessonId));
  }

  @Test
  void addToCourse_shouldThrowIfCourseNotFound() {
    when(lessonRepository.findById(this.lessonId)).thenReturn(Optional.of(lesson));
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class,
        () -> lessonService.addToCourse(courseId, lessonId));
  }
}