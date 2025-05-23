package com.leverx.learning_management_system.course.service.imp;

import static com.leverx.learning_management_system.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learning_management_system.ConstMessages.STUDENTS_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.leverx.learning_management_system.course.Course;
import com.leverx.learning_management_system.course.CourseRepository;
import com.leverx.learning_management_system.course.dto.CourseDto;
import com.leverx.learning_management_system.course.dto.CreateCourseDto;
import com.leverx.learning_management_system.course.dto.DetailedCourseDto;
import com.leverx.learning_management_system.course.dto.UpdateCourseDto;
import com.leverx.learning_management_system.courseSettings.CourseSettings;
import com.leverx.learning_management_system.mailtrap.imp.MailTrapImp;
import com.leverx.learning_management_system.mapper.CourseMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
class CourseServiceImpTest {

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private CourseMapper courseMapper;

  @Mock
  private MailTrapImp mailTrapImp;

  @InjectMocks
  private CourseServiceImp courseService;

  private UUID courseId;
  private Course course;

  @Captor
  private ArgumentCaptor<Course> courseCaptor;

  @BeforeEach
  void setup() {
    courseId = UUID.randomUUID();
    course = Course.builder()
        .id(courseId)
        .title("Test Course")
        .description("Test Description")
        .price(BigDecimal.valueOf(100.0))
        .settings(CourseSettings.builder()
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(10))
            .isPublic(true)
            .build())
        .build();
  }

  @Test
  void createCourse_shouldSaveAndReturnDto() {
    // Given
    var dto = CreateCourseDto.builder()
        .title("Intro to Java programming")
        .description("Description")
        .price(BigDecimal.valueOf(120))
        .isPublic(true)
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusDays(5))
        .build();
    var savedCourse = Course.builder().title(dto.getTitle()).build();
    var courseDto = new CourseDto();
    when(courseRepository.save(any())).thenReturn(savedCourse);
    when(courseMapper.toDto(savedCourse)).thenReturn(courseDto);

    // When
    var result = courseService.createCourse(dto);

    // Then
    verify(courseRepository).save(courseCaptor.capture());
    var captured = courseCaptor.getValue();
    assertEquals(dto.getTitle(), captured.getTitle());
    assertEquals(courseDto, result);
  }

  @Test
  void getCourseById_shouldReturnCourseDto_whenExists() {
    // Given
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(courseMapper.toDto(course)).thenReturn(new CourseDto());

    // When
    var result = courseService.getCourseById(courseId);

    // Then
    assertNotNull(result);
    verify(courseRepository).findById(courseId);
  }

  @Test
  void getCourseById_shouldReturnNull_whenNotFound() {
    // Given
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    // When
    var result = courseService.getCourseById(courseId);

    // Then
    assertNull(result);
  }

  @Test
  void getAllCourses_shouldReturnMappedCourses() {
    // Given
    when(courseRepository.findAll()).thenReturn(List.of(course));
    when(courseMapper.toDto(course)).thenReturn(new CourseDto());

    // When
    var result = courseService.getAllCourses();

    // Then
    assertEquals(1, result.size());
    verify(courseRepository).findAll();
  }

  @Test
  void deleteById_shouldDeleteCourse_whenExists() {
    // Given
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    // When
    courseService.deleteById(courseId);

    // Then
    verify(courseRepository).delete(course);
  }

  @Test
  void deleteById_shouldThrowException_whenNotFound() {
    // Given
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    // When / Then
    assertThrows(ResponseStatusException.class, () -> courseService.deleteById(courseId));
  }

  @Test
  void updateCourse_shouldUpdateAndReturnDto() {
    // Given
    var updateDto = UpdateCourseDto.builder()
        .id(courseId)
        .title("new")
        .description("Desc")
        .price(BigDecimal.valueOf(150))
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusDays(1))
        .isPublic(false)
        .build();
    var settings = CourseSettings.builder()
        .startDate(LocalDateTime.now().minusDays(1))
        .endDate(LocalDateTime.now())
        .isPublic(false).build();
    var existingCourse  = Course.builder()
        .id(courseId)
        .title("old")
        .description("desc2")
        .price(BigDecimal.ZERO)
        .settings(settings)
        .build();
    var updatedCourse = Course.builder().id(courseId).build();
    var expectedDto = new CourseDto();
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
    when(courseRepository.save(any())).thenReturn(updatedCourse);
    when(courseMapper.toDto(updatedCourse)).thenReturn(expectedDto);

    // When
    var result = courseService.updateCourse(updateDto);

    // Then
    verify(courseRepository).save(courseCaptor.capture());
    var savedCourse = courseCaptor.getValue();
    assertEquals(updateDto.getTitle(), savedCourse.getTitle());
    assertEquals(updateDto.getDescription(), savedCourse.getDescription());
    assertEquals(updateDto.getPrice(), savedCourse.getPrice());
    assertEquals(updateDto.getStartDate(), savedCourse.getSettings().getStartDate());
    assertEquals(updateDto.getEndDate(), savedCourse.getSettings().getEndDate());
    assertEquals(updateDto.getIsPublic(), savedCourse.getSettings().getIsPublic());
    assertEquals(expectedDto, result);
  }

  @Test
  void updateCourse_shouldThrowException_whenNotFound() {
    // Given
    var dto = UpdateCourseDto.builder()
        .id(courseId)
        .title("Title")
        .description("Desc")
        .price(BigDecimal.valueOf(99.0))
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusDays(1))
        .isPublic(true)
        .build();
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    // When / Then
    assertThrows(ResponseStatusException.class, () -> courseService.updateCourse(dto));
  }

  @Test
  void getMostPopularCourses_shouldReturnTopMappedCourses() {
    // Given
    when(courseRepository.findTop5MostPopular()).thenReturn(List.of(course));
    when(courseMapper.toDto(course)).thenReturn(new CourseDto());

    // When
    var result = courseService.getMostPopularCourses();

    // Then
    assertEquals(1, result.size());
    verify(courseRepository).findTop5MostPopular();
  }

  @Test
  void getDetailedCourseById_shouldReturnDetailedDto_whenFound() {
    // Given
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(courseMapper.toDetailedDto(course)).thenReturn(new DetailedCourseDto());

    // When
    var result = courseService.getDetailedCourseById(courseId);

    // Then
    assertNotNull(result);
  }

  @Test
  void getDetailedCourseById_shouldReturnNull_whenNotFound() {
    // Given
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    // When
    var result = courseService.getDetailedCourseById(courseId);

    // Then
    assertNull(result);
  }

  @Test
  void printAllCoursesByStartDateBetween_shouldCallRepository() {
    // Given
    var start = LocalDateTime.now();
    var end = start.plusDays(3);
    when(courseRepository.findAllByStartDateBetween(start, end)).thenReturn(List.of(course));

    // When
    courseService.printAllCoursesByStartDateBetween(start, end);

    // Then
    verify(courseRepository).findAllByStartDateBetween(start, end);
  }

  @Test
  void sendMailToEnrolledStudents_shouldThrowException_whenCourseNotFound() {
    // Given
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    // When / Then
    var exception = assertThrows(ResponseStatusException.class,
        () -> courseService.sendMailToEnrolledStudents(courseId)
    );
    assertTrue(exception.getMessage().contains(String.format(COURSE_NOT_FOUND,courseId)));
  }

  @Test
  void sendMailToEnrolledStudents_shouldThrowException_whenStudentsListIsEmpty() {
    // Given
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    // When / Then
    var exception = assertThrows(ResponseStatusException.class,
        () -> courseService.sendMailToEnrolledStudents(courseId)
    );
    assertTrue(exception.getMessage().contains(STUDENTS_NOT_FOUND));
  }
}
