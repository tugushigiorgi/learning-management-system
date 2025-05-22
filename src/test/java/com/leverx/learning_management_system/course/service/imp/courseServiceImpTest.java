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
    var dto = CreateCourseDto.builder()
        .title("Intro to Java programming")
        .description("Description")
        .price(BigDecimal.valueOf(120))
        .isPublic(true)
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusDays(5))
        .build();
    var savedCourse = Course.builder()
        .title(dto.getTitle())
        .build();
    var courseDto = new CourseDto();
    when(courseRepository.save(any())).thenReturn(savedCourse);
    when(courseMapper.toDto(savedCourse)).thenReturn(courseDto);
    var result = courseService.createCourse(dto);
    verify(courseRepository).save(courseCaptor.capture());
    var captured = courseCaptor.getValue();
    assertEquals(dto.getTitle(), captured.getTitle());
    assertEquals(courseDto, result);
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
  void updateCourse_shouldUpdateAndReturnDto() {
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
    var result = courseService.updateCourse(updateDto);
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
  void getMostPopularCourses_shouldReturnTopMappedCourses() {
    when(courseRepository.findTop5MostPopular())
        .thenReturn(List.of(course));
    when(courseMapper.toDto(course)).thenReturn(new CourseDto());
    var result = courseService.getMostPopularCourses();
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
  void printAllCoursesByStartDateBetween_shouldCallRepository() {
    var start = LocalDateTime.now();
    var end = start.plusDays(3);
    when(courseRepository.findAllByStartDateBetween(start, end))
        .thenReturn(List.of(course));
    courseService.printAllCoursesByStartDateBetween(start, end);
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
