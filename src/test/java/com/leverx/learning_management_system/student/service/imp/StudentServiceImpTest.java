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
  void createStudent_shouldSaveStudent() {
    var dto = mock(CreateStudentDto.class);
    studentService.createStudent(dto);
    verify(studentRepository).save(any(Student.class));
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
  void updateStudent_shouldUpdateAndSave() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    studentService.updateStudent(updateStudentDto);
    assertEquals(updateStudentDto.getFirstName(), student.getFirstName());
    assertEquals(updateStudentDto.getLastName(), student.getLastName());
    assertEquals(updateStudentDto.getEmail(), student.getEmail());
    assertEquals(updateStudentDto.getDateOfBirth(), student.getDateOfBirth());
    verify(studentRepository).save(student);
  }

  @Test
  void updateStudent_shouldThrow_ifNotFound() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
    var exception = assertThrows(ResponseStatusException.class, () ->
        studentService.updateStudent(updateStudentDto));
    assertTrue(exception.getMessage().contains(STUDENT_NOT_FOUND));
  }
}
