package com.leverx.learningmanagementsystem.student.service.impl;

import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learningmanagementsystem.ConstMessages.NOT_ENOUGH_COINS;
import static com.leverx.learningmanagementsystem.ConstMessages.STUDENT_ALREADY_ENROLLED;
import static com.leverx.learningmanagementsystem.ConstMessages.STUDENT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.learningmanagementsystem.course.Course;
import com.leverx.learningmanagementsystem.course.CourseRepository;
import com.leverx.learningmanagementsystem.mapper.StudentMapper;
import com.leverx.learningmanagementsystem.student.Student;
import com.leverx.learningmanagementsystem.student.StudentRepository;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentDto;
import com.leverx.learningmanagementsystem.student.dto.UpdateStudentDto;
import jakarta.mail.MessagingException;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private StudentMapper studentMapper;

  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private StudentServiceImpl studentService;

  @Captor
  private ArgumentCaptor<Student> studentCaptor;

  private UpdateStudentDto updateStudentDto;
  private UUID studentId;
  private UUID courseId;
  private Student student;
  private Course course;

  @BeforeEach
  void setUp() {
    studentId = UUID.randomUUID();
    courseId = UUID.randomUUID();
    updateStudentDto = UpdateStudentDto.builder()
        .id(studentId)
        .firstName("New")
        .lastName("Name")
        .email("new@mail.com")
        .dateOfBirth(LocalDate.of(1990, 1, 1))
        .build();

    course = Course.builder()
        .id(courseId)
        .price(BigDecimal.valueOf(50))
        .build();

    student = Student.builder()
        .id(studentId)
        .coins(BigDecimal.valueOf(100))
        .courses(new HashSet<>())
        .build();
  }
  @Test
  void getEnrolledCourseCount_shouldReturnSize() {
    // Arrange
    student.getCourses().add(course);
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    // Act
    var count = studentService.getEnrolledCourseCount(studentId);

    // Assert
    assertEquals(1, count);
  }

  @Test
  void getEnrolledCourseCount_shouldThrowException_ifStudentNotFound() {
    // Arrange
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    // Act & Assert
    var exception = assertThrows(ResponseStatusException.class,
        () -> studentService.getEnrolledCourseCount(studentId));
    assertEquals(String.format(STUDENT_NOT_FOUND,studentId), exception.getReason());
    assertEquals(NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void enrollToCourse_shouldSucceed_whenEnoughCoinsAndNotEnrolled() throws MessagingException {
    // Arrange
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    // Act
    studentService.enrollToCourse(studentId, courseId);

    // Assert
    assertTrue(student.getCourses().contains(course));
    assertEquals(BigDecimal.valueOf(50), student.getCoins());
    verify(studentRepository).save(student);
  }

  @Test
  void enrollToCourse_shouldThrow_ifStudentAlreadyEnrolled() {
    // Arrange
    student.getCourses().add(course);
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    // Act & Assert
    var exception = assertThrows(ResponseStatusException.class,
        () -> studentService.enrollToCourse(studentId, courseId));
    assertEquals(STUDENT_ALREADY_ENROLLED, exception.getReason());
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
  }

  @Test
  void enrollToCourse_shouldThrow_ifNotEnoughCoins() {
    // Arrange
    student.setCoins(BigDecimal.valueOf(10));
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    // Act & Assert
    var exception = assertThrows(ResponseStatusException.class,
        () -> studentService.enrollToCourse(studentId, courseId));
    assertEquals(NOT_ENOUGH_COINS, exception.getReason());
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
  }

  @Test
  void enrollToCourse_shouldThrow_ifStudentNotFound() {
    // Arrange
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    // Act & Assert
    var exception = assertThrows(ResponseStatusException.class,
        () -> studentService.enrollToCourse(studentId, courseId));
    assertEquals(String.format(STUDENT_NOT_FOUND,studentId), exception.getReason());
    assertEquals(NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void enrollToCourse_shouldThrow_ifCourseNotFound() {
    // Arrange
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    // Act & Assert
    var exception = assertThrows(ResponseStatusException.class,
        () -> studentService.enrollToCourse(studentId, courseId));
    assertEquals(String.format(COURSE_NOT_FOUND,courseId), exception.getReason());
    assertEquals(NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void createStudent_shouldSaveAndReturnDto() {
    // Arrange
    var dto = CreateStudentDto.builder()
        .firstName("Giorgi")
        .lastName("Tughushi")
        .email("giorgi@gmail.com")
        .dateOfBirth(LocalDate.of(2001, 1, 1))
        .build();

    var savedStudent = Student.builder()
        .id(UUID.randomUUID())
        .firstName("Giorgi")
        .lastName("Tughushi")
        .email("giorgi@gmail.com")
        .dateOfBirth(LocalDate.of(2001, 1, 1))
        .build();

    var expectedDto = StudentDto.builder()
        .firstName("Giorgi")
        .lastName("Tughushi")
        .email("giorgi@gmail.com")
        .dateOfBirth(LocalDate.of(2001, 1, 1))
        .build();

    when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
    when(studentMapper.toDto(savedStudent)).thenReturn(expectedDto);

    // Act
    var result = studentService.createStudent(dto);

    // Assert
    verify(studentRepository).save(studentCaptor.capture());
    var capturedStudent = studentCaptor.getValue();
    assertEquals("Giorgi", capturedStudent.getFirstName());
    assertEquals("Tughushi", capturedStudent.getLastName());
    assertEquals("giorgi@gmail.com", capturedStudent.getEmail());
    assertEquals(LocalDate.of(2001, 1, 1), capturedStudent.getDateOfBirth());
    assertEquals(expectedDto, result);
  }

  @Test
  void getStudentById_shouldReturnMappedStudent() {
    // Arrange
    var dto = StudentDto.builder()
        .firstName("Test")
        .lastName("User")
        .email("test@example.com")
        .dateOfBirth(LocalDate.of(2000, 1, 1))
        .build();
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(studentMapper.toDto(student)).thenReturn(dto);

    // Act
    var result = studentService.getStudentById(studentId);

    // Assert
    assertEquals(dto, result);
  }

  @Test
  void getStudentById_shouldReturnNull_whenNotFound() {
    // Arrange
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    // Act
    var result = studentService.getStudentById(studentId);

    // Assert
    assertNull(result);
  }

  @Test
  void getAllStudents_shouldReturnMappedList() {
    // Arrange
    var mapped = StudentDto.builder().firstName("Mapped").build();
    when(studentRepository.findAll()).thenReturn(List.of(student));
    when(studentMapper.toDto(any())).thenReturn(mapped);

    // Act
    var result = studentService.getAllStudents();

    // Assert
    assertEquals(List.of(mapped), result);
  }

  @Test
  void deleteById_shouldDeleteStudent() {
    // Arrange
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    // Act
    studentService.deleteById(studentId);

    // Assert
    verify(studentRepository).delete(student);
  }

  @Test
  void deleteById_shouldThrow_ifNotFound() {
    // Arrange
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    // Act & Assert
    var exception = assertThrows(ResponseStatusException.class,
        () -> studentService.deleteById(studentId));
    assertEquals(String.format(STUDENT_NOT_FOUND,studentId), exception.getReason());
    assertEquals(NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void updateStudent_shouldUpdateAndReturnDto() {
    // Arrange
    var existingStudent = Student.builder()
        .id(studentId)
        .firstName("Old")
        .lastName("Name")
        .email("old@email.com")
        .dateOfBirth(LocalDate.of(1995, 1, 1))
        .build();

    var updateDto = UpdateStudentDto.builder()
        .id(studentId)
        .firstName("New")
        .lastName("User")
        .email("new@email.com")
        .dateOfBirth(LocalDate.of(2000, 5, 5))
        .build();

    var updatedStudent = Student.builder()
        .id(studentId)
        .firstName("New")
        .lastName("User")
        .email("new@email.com")
        .dateOfBirth(LocalDate.of(2000, 5, 5))
        .build();

    var expectedDto = StudentDto.builder()
        .firstName("New")
        .lastName("User")
        .email("new@email.com")
        .dateOfBirth(LocalDate.of(2000, 5, 5))
        .build();

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
    when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);
    when(studentMapper.toDto(updatedStudent)).thenReturn(expectedDto);

    // Act
    var result = studentService.updateStudent(updateDto);

    // Assert
    verify(studentRepository).save(studentCaptor.capture());
    var captured = studentCaptor.getValue();
    assertEquals("New", captured.getFirstName());
    assertEquals("User", captured.getLastName());
    assertEquals("new@email.com", captured.getEmail());
    assertEquals(LocalDate.of(2000, 5, 5), captured.getDateOfBirth());
    assertEquals(expectedDto, result);
  }

  @Test
  void updateStudent_shouldThrow_ifNotFound() {
    // Arrange
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    // Act & Assert
    var exception = assertThrows(ResponseStatusException.class,
        () -> studentService.updateStudent(updateStudentDto));
    assertEquals(String.format(STUDENT_NOT_FOUND,studentId), exception.getReason());
    assertEquals(NOT_FOUND, exception.getStatusCode());
    verify(studentRepository, never()).save(any());
  }

}
