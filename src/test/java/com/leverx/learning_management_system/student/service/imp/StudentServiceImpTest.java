package com.leverx.learning_management_system.student.service.imp;

import static com.leverx.learning_management_system.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learning_management_system.ConstMessages.NOT_ENOUGH_COINS;
import static com.leverx.learning_management_system.ConstMessages.STUDENT_ALREADY_ENROLLED;
import static com.leverx.learning_management_system.ConstMessages.STUDENT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.leverx.learning_management_system.course.Course;
import com.leverx.learning_management_system.course.CourseRepository;
import com.leverx.learning_management_system.mapper.StudentMapper;
import com.leverx.learning_management_system.student.Student;
import com.leverx.learning_management_system.student.StudentRepository;
import com.leverx.learning_management_system.student.dto.CreateStudentDto;
import com.leverx.learning_management_system.student.dto.StudentDto;
import com.leverx.learning_management_system.student.dto.UpdateStudentDto;
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
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class StudentServiceImpTest {

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private StudentMapper studentMapper;

  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private StudentServiceImp studentService;

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
    student.getCourses().add(course);
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    var count = studentService.getEnrolledCourseCount(studentId);
    assertEquals(1, count);
  }

  @Test
  void getEnrolledCourseCount_shouldThrowException_ifStudentNotFound() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
    var exception = assertThrows(ResponseStatusException.class, () ->
        studentService.getEnrolledCourseCount(studentId));
    assertTrue(exception.getMessage().contains(STUDENT_NOT_FOUND));
  }

  @Test
  void enrollToCourse_shouldSucceed_whenEnoughCoinsAndNotEnrolled() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    studentService.enrollToCourse(studentId, courseId);
    assertTrue(student.getCourses().contains(course));
    assertEquals(BigDecimal.valueOf(50), student.getCoins());
    verify(studentRepository).save(student);
  }

  @Test
  void enrollToCourse_shouldThrow_ifStudentAlreadyEnrolled() {
    student.getCourses().add(course);
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    var exception = assertThrows(ResponseStatusException.class, () ->
        studentService.enrollToCourse(studentId, courseId));
    assertTrue(exception.getMessage().contains(STUDENT_ALREADY_ENROLLED));
  }

  @Test
  void enrollToCourse_shouldThrow_ifNotEnoughCoins() {
    student.setCoins(BigDecimal.valueOf(10));
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    var exception = assertThrows(ResponseStatusException.class, () ->
        studentService.enrollToCourse(studentId, courseId));
    assertTrue(exception.getMessage().contains(NOT_ENOUGH_COINS));
  }

  @Test
  void enrollToCourse_shouldThrow_ifStudentNotFound() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
    var exception = assertThrows(ResponseStatusException.class, () ->
        studentService.enrollToCourse(studentId, courseId));
    assertTrue(exception.getMessage().contains(STUDENT_NOT_FOUND));
  }

  @Test
  void enrollToCourse_shouldThrow_ifCourseNotFound() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
    var exception = assertThrows(ResponseStatusException.class, () ->
        studentService.enrollToCourse(studentId, courseId));
    assertTrue(exception.getMessage().contains(COURSE_NOT_FOUND));
  }

  @Test
  void createStudent_shouldSaveAndReturnDto() {
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
    var result = studentService.createStudent(dto);
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
    var dto = mock(StudentDto.class);
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(studentMapper.toDto(student)).thenReturn(dto);
    var result = studentService.getStudentById(studentId);
    assertEquals(dto, result);
  }

  @Test
  void getStudentById_shouldReturnNull_whenNotFound() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
    var result = studentService.getStudentById(studentId);
    assertNull(result);
  }

  @Test
  void getAllStudents_shouldReturnMappedList() {
    var list = List.of(student);
    var mappedList = List.of(mock(StudentDto.class));
    when(studentRepository.findAll()).thenReturn(list);
    when(studentMapper.toDto(any())).thenReturn(mappedList.get(0));
    var result = studentService.getAllStudents();
    assertEquals(mappedList, result);
  }

  @Test
  void deleteById_shouldDeleteStudent() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    studentService.deleteById(studentId);
    verify(studentRepository).delete(student);
  }

  @Test
  void deleteById_shouldThrow_ifNotFound() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
    var exception = assertThrows(ResponseStatusException.class, () ->
        studentService.deleteById(studentId));
    assertTrue(exception.getMessage().contains(STUDENT_NOT_FOUND));
  }

  @Test
  void updateStudent_shouldUpdateAndReturnDto() {
    var studentId = UUID.randomUUID();
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
    var result = studentService.updateStudent(updateDto);
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
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
    var exception = assertThrows(ResponseStatusException.class, () ->
        studentService.updateStudent(updateStudentDto));
    assertTrue(exception.getMessage().contains(STUDENT_NOT_FOUND));
  }
}
