package com.leverx.learningmanagementsystem.student.service.imp;

import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learningmanagementsystem.ConstMessages.NOT_ENOUGH_COINS;
import static com.leverx.learningmanagementsystem.ConstMessages.STUDENT_ALREADY_ENROLLED;
import static com.leverx.learningmanagementsystem.ConstMessages.STUDENT_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.learningmanagementsystem.course.CourseRepository;
import com.leverx.learningmanagementsystem.mapper.StudentMapper;
import com.leverx.learningmanagementsystem.student.Student;
import com.leverx.learningmanagementsystem.student.StudentRepository;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentDto;
import com.leverx.learningmanagementsystem.student.dto.UpdateStudentDto;
import com.leverx.learningmanagementsystem.student.service.StudentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class StudentServiceImp implements StudentService {

  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;
  private final CourseRepository courseRepository;

  @Override
  @Transactional(readOnly = true)
  public Integer getEnrolledCourseCount(UUID studentId) {
    var getStudent = studentRepository.findById(studentId)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, String.format(STUDENT_NOT_FOUND, studentId)));
    return getStudent.getCourses().size();
  }

  @Override
  @Transactional
  public void enrollToCourse(UUID studentId, UUID courseId) {
    var getStudent = studentRepository.findById(studentId)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, String.format(STUDENT_NOT_FOUND, studentId)));
    var getCourse = courseRepository.findById(courseId)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, courseId)));
    var alreadyEnrolled = getStudent.getCourses()
        .stream()
        .anyMatch(course -> course.getId().equals(courseId));
    if (alreadyEnrolled) {
      throw new ResponseStatusException(BAD_REQUEST, STUDENT_ALREADY_ENROLLED);
    }
    if (getStudent.getCoins().compareTo(getCourse.getPrice()) >= 0) {
      getStudent.getCourses().add(getCourse);
      getStudent.setCoins(getStudent.getCoins().subtract(getCourse.getPrice()));
      studentRepository.save(getStudent);
    } else {
      throw new ResponseStatusException(BAD_REQUEST, NOT_ENOUGH_COINS);
    }
  }

  @Transactional
  @Override
  public StudentDto createStudent(CreateStudentDto studentDto) {
    var newStudent = Student.builder()
        .firstName(studentDto.getFirstName())
        .lastName(studentDto.getLastName())
        .email(studentDto.getEmail())
        .dateOfBirth(studentDto.getDateOfBirth())
        .build();
    var savedStudent=studentRepository.save(newStudent);
    return studentMapper.toDto(savedStudent);
  }

  @Override
  @Transactional(readOnly = true)
  public StudentDto getStudentById(UUID id) {
    return studentRepository.findById(id)
        .map(studentMapper::toDto)
        .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public List<StudentDto> getAllStudents() {
    return studentRepository.findAll()
        .stream()
        .map(studentMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    var getStudent = studentRepository.findById(id)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, String.format(STUDENT_NOT_FOUND, id)));
    studentRepository.delete(getStudent);
  }

  @Override
  @Transactional
  public StudentDto updateStudent(UpdateStudentDto studentDto) {
    var currentStudent = studentRepository.findById(studentDto.getId())
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, String.format(STUDENT_NOT_FOUND, studentDto.getId())));
    if (!studentDto.getFirstName().equals(currentStudent.getFirstName())) {
      currentStudent.setFirstName(studentDto.getFirstName());
    }
    if (!studentDto.getLastName().equals(currentStudent.getLastName())) {
      currentStudent.setLastName(studentDto.getLastName());
    }
    if (!studentDto.getEmail().equals(currentStudent.getEmail())) {
      currentStudent.setEmail(studentDto.getEmail());
    }
    if (!studentDto.getDateOfBirth().equals(currentStudent.getDateOfBirth())) {
      currentStudent.setDateOfBirth(studentDto.getDateOfBirth());
    }
    var updatedStudent = studentRepository.save(currentStudent);
    return studentMapper.toDto(updatedStudent);
  }
}
