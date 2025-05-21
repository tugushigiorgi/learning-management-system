package com.leverx.learning_management_system.course.service.imp;

import static com.leverx.learning_management_system.ConstMessages.COURSE_NOT_FOUND_WITH_STATUS_CODE_ERROR;
import static com.leverx.learning_management_system.ConstMessages.STUDENTS_NOT_FOUND_WITH_STATUS_CODE_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.leverx.learning_management_system.mailtrap.imp.mailTrapImp;
import com.leverx.learning_management_system.mapper.CourseMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
class CourseServiceImpTest {

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private CourseMapper courseMapper;

  @Mock
  private mailTrapImp mailTrapImp;

  @InjectMocks
  private courseServiceImp courseService;

  private UUID courseId;
  private Course course;

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
  void createCourse_shouldSaveCourse() {
    var dto = CreateCourseDto.builder()
        .title("Intro to Java programming")
        .description("Description")
        .price(BigDecimal.valueOf(120))
        .isPublic(true)
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusDays(5))
        .build();
    courseService.createCourse(dto);
    verify(courseRepository).save(any(Course.class));
  }

  @Test
  void getCourseById_shouldReturnCourseDto_whenExists() {
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(courseMapper.toDto(course)).thenReturn(new CourseDto());
    var result = courseService.getCourseById(courseId);
    assertNotNull(result);
    verify(courseRepository).findById(courseId);
  }

  @Test
  void getCourseById_shouldReturnNull_whenNotFound() {
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
    var result = courseService.getCourseById(courseId);
    assertNull(result);
  }

  @Test
  void getAllCourses_shouldReturnMappedCourses() {
    when(courseRepository.findAll()).thenReturn(List.of(course));
    when(courseMapper.toDto(course)).thenReturn(new CourseDto());
    var result = courseService.getAllCourses();
    assertEquals(1, result.size());
    verify(courseRepository).findAll();
  }

  @Test
  void deleteById_shouldDeleteCourse_whenExists() {
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    courseService.deleteById(courseId);
    verify(courseRepository).delete(course);
  }

  @Test
  void deleteById_shouldThrowException_whenNotFound() {
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class, () -> courseService.deleteById(courseId));
  }

  @Test
  void updateCourse_shouldUpdateAndSaveCourse() {
    var dto = UpdateCourseDto.builder()
        .id(courseId)
        .title("Updated Title")
        .description("Updated Desc")
        .price(BigDecimal.valueOf(150))
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusDays(1))
        .isPublic(false)
        .build();
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    courseService.updateCourse(dto);
    assertEquals("Updated Title", course.getTitle());
    assertEquals("Updated Desc", course.getDescription());
    assertEquals(BigDecimal.valueOf(150), course.getPrice());
    assertEquals(false, course.getSettings().getIsPublic());
    verify(courseRepository).save(course);
  }

  @Test
  void updateCourse_shouldThrowException_whenNotFound() {
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
    assertThrows(ResponseStatusException.class, () -> courseService.updateCourse(dto));
  }

  @Test
  void mostPopularCourses_shouldReturnTopMappedCourses() {
    when(courseRepository.findTop5MostPopular())
        .thenReturn(List.of(course));
    when(courseMapper.toDto(course)).thenReturn(new CourseDto());
    var result = courseService.mostPopularCourses();
    assertEquals(1, result.size());
    verify(courseRepository).findTop5MostPopular();
  }

  @Test
  void getDetailedCourseById_shouldReturnDetailedDto_whenFound() {
    when(courseRepository.findById(courseId))
        .thenReturn(Optional.of(course));
    when(courseMapper.toDetailedDto(course))
        .thenReturn(new DetailedCourseDto());
    var result = courseService.getDetailedCourseById(courseId);
    assertNotNull(result);
  }

  @Test
  void getDetailedCourseById_shouldReturnNull_whenNotFound() {
    when(courseRepository.findById(courseId))
        .thenReturn(Optional.empty());
    var result = courseService.getDetailedCourseById(courseId);
    assertNull(result);
  }

  @Test
  void printAlCourseByStartDateBetween_shouldCallRepository() {
    var start = LocalDateTime.now();
    var end = start.plusDays(3);
    when(courseRepository.findAllByStartDateBetween(start, end))
        .thenReturn(List.of(course));
    courseService.printAlCourseByStartDateBetween(start, end);
    verify(courseRepository).findAllByStartDateBetween(start, end);
  }

  @Test
  void sendMailToEnrolledStudents_shouldThrowException_whenCourseNotFound() {
    when(courseRepository.findById(courseId))
        .thenReturn(Optional.empty());
    var exception = assertThrows(ResponseStatusException.class,
        () -> courseService.sendMailToEnrolledStudents(courseId)
    );
    assertEquals(COURSE_NOT_FOUND_WITH_STATUS_CODE_ERROR + courseId + "\"", exception.getMessage());
  }

  @Test
  void sendMailToEnrolledStudents_shouldThrowException_whenStudentsListIsEmpty() {
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    var exception = assertThrows(ResponseStatusException.class,
        () -> courseService.sendMailToEnrolledStudents(courseId)
    );
    assertEquals(STUDENTS_NOT_FOUND_WITH_STATUS_CODE_ERROR, exception.getMessage());
  }
}
