package com.leverx.learningmanagementsystem.lesson.service.impl;

import static com.leverx.learningmanagementsystem.ConstMessages.LESSON_ALREADY_ADDED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.learningmanagementsystem.course.Course;
import com.leverx.learningmanagementsystem.course.CourseRepository;
import com.leverx.learningmanagementsystem.lesson.Lesson;
import com.leverx.learningmanagementsystem.lesson.LessonRepository;
import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.UpdateLessonDto;
import com.leverx.learningmanagementsystem.mapper.LessonMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

  @Mock
  private LessonRepository lessonRepository;

  @Mock
  private LessonMapper lessonMapper;

  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private LessonServiceImpl lessonService;

  @Captor
  private ArgumentCaptor<Lesson> lessonCaptor;

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
  void createLesson_shouldCreateAndReturnLessonDto() {
    // Given
    var courseId = UUID.randomUUID();

    var dto = CreateLessonDto.builder()
        .title("Intro to Java programming")
        .duration(11)
        .courseId(courseId)
        .build();

    var course = Course.builder()
        .id(courseId)
        .title("Java Course")
        .lessons(new HashSet<>())
        .build();

    var mappedLesson = Lesson.builder()
        .title(dto.getTitle())
        .duration(dto.getDuration())
        .build();

    var savedLesson = Lesson.builder()
        .id(UUID.randomUUID())
        .title(dto.getTitle())
        .duration(dto.getDuration())
        .build();

    var expectedDto = LessonDto.builder()
        .title(savedLesson.getTitle())
        .duration(savedLesson.getDuration())
        .build();

    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(lessonMapper.toEntity(dto)).thenReturn(mappedLesson);
    when(lessonRepository.save(mappedLesson)).thenReturn(savedLesson);
    when(lessonMapper.toDto(savedLesson)).thenReturn(expectedDto);

    // When
    var actualDto = lessonService.createLesson(dto);

    // Then
    verify(courseRepository).findById(courseId);
    verify(lessonRepository).save(lessonCaptor.capture());
    var capturedLesson = lessonCaptor.getValue();

    assertEquals(dto.getTitle(), capturedLesson.getTitle());
    assertEquals(dto.getDuration(), capturedLesson.getDuration());
    assertEquals(expectedDto, actualDto);

  }

  @Test
  void getLessonById_shouldReturnLessonDtoIfFound() {
    // Given
    var dto = LessonDto.builder()
        .title("test")
        .duration(30)
        .build();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
    when(lessonMapper.toDto(lesson)).thenReturn(dto);

    // When
    var result = lessonService.getLessonById(lessonId);

    // Then
    assertEquals(dto, result);
  }

  @Test
  void getLessonById_shouldReturnNullIfNotFound() {
    // Given
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

    // When & Then
    assertNull(lessonService.getLessonById(lessonId));
  }

  @Test
  void getAllLessons_shouldReturnAllLessons() {
    // Given
    var lessons = List.of(lesson);
    var dtos = List.of(LessonDto.builder()
        .title("test")
        .duration(30)
        .build());
    when(lessonRepository.findAll()).thenReturn(lessons);
    when(lessonMapper.toDto(lessons.getFirst())).thenReturn(dtos.getFirst());

    // When
    List<LessonDto> result = lessonService.getAllLessons();

    // Then
    assertEquals(dtos, result);
  }

  @Test
  void deleteById_shouldDeleteIfFound() {
    // Given
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

    // When
    lessonService.deleteById(lessonId);

    // Then
    verify(lessonRepository).delete(lesson);
  }

  @Test
  void deleteById_shouldThrowIfNotFound() {
    // Given
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

    // When & Then
    var ex = assertThrows(ResponseStatusException.class,
        () -> lessonService.deleteById(lessonId));
    assertEquals(NOT_FOUND, ex.getStatusCode());
  }

  @Test
  void updateLessons_shouldUpdateAndReturnDto() {
    // Given
    var dto = UpdateLessonDto.builder()
        .title("Updated Title")
        .duration(45)
        .build();

    var existingLesson = Lesson.builder()
        .id(lessonId)
        .title("Old Title")
        .duration(30)
        .build();

    var expectedDto = LessonDto.builder()
        .title("Updated Title")
        .duration(45)
        .build();

    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(existingLesson));
    when(lessonRepository.save(existingLesson)).thenReturn(existingLesson);
    when(lessonMapper.toDto(existingLesson)).thenReturn(expectedDto);


    doAnswer(invocation -> {
      UpdateLessonDto source = invocation.getArgument(0);
      Lesson target = invocation.getArgument(1);
      target.setTitle(source.getTitle());
      target.setDuration(source.getDuration());
      return null;
    }).when(lessonMapper).update(eq(dto), same(existingLesson));

    // When
    var result = lessonService.updateLessons(lessonId, dto);

    // Then
    verify(lessonRepository).save(lessonCaptor.capture());
    var savedLesson = lessonCaptor.getValue();

    assertEquals("Updated Title", savedLesson.getTitle());
    assertEquals(45, savedLesson.getDuration());
    assertEquals(expectedDto, result);
  }


  @Test
  void updateLessons_shouldThrowIfNotFound() {
    // Given
    var dto = UpdateLessonDto.builder()
        .title("programming course")
        .duration(12)
        .build();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResponseStatusException.class,
        () -> lessonService.updateLessons(lessonId, dto));
  }

  @Test
  void addToCourse_shouldAddLessonToCourse() {
    // Given
    var course = Course.builder()
        .id(courseId)
        .lessons(new HashSet<>())
        .build();
    when(lessonRepository.findById(lessonId))
        .thenReturn(Optional.of(lesson));
    when(courseRepository.findById(courseId))
        .thenReturn(Optional.of(course));

    // When
    lessonService.addToCourse(courseId, lessonId);

    // Then
    assertTrue(course.getLessons().contains(lesson));
    verify(courseRepository).save(course);
  }

  @Test
  void addToCourse_shouldThrowIfLessonAlreadyAdded() {
    // Given
    var lessons = new HashSet<Lesson>();
    lessons.add(lesson);
    var course = Course
        .builder()
        .id(courseId)
        .lessons(lessons)
        .build();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    // When & Then
    var ex = assertThrows(ResponseStatusException.class,
        () -> lessonService.addToCourse(courseId, lessonId));
    assertEquals(BAD_REQUEST, ex.getStatusCode());
    assertEquals(LESSON_ALREADY_ADDED, ex.getReason());
  }

  @Test
  void addToCourse_shouldThrowIfLessonNotFound() {
    // Given
    when(lessonRepository.findById(lessonId))
        .thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResponseStatusException.class,
        () -> lessonService.addToCourse(courseId, lessonId));
  }

  @Test
  void addToCourse_shouldThrowIfCourseNotFound() {
    // Given
    when(lessonRepository.findById(this.lessonId)).thenReturn(Optional.of(lesson));
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResponseStatusException.class,
        () -> lessonService.addToCourse(courseId, lessonId));
  }
}