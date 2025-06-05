package com.leverx.learningmanagementsystem.student.service.impl;

import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_ENROLLED_SUBJECT;
import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learningmanagementsystem.ConstMessages.FROM_MAIL;
import static com.leverx.learningmanagementsystem.ConstMessages.NOT_ENOUGH_COINS;
import static com.leverx.learningmanagementsystem.ConstMessages.STUDENT_ALREADY_ENROLLED;
import static com.leverx.learningmanagementsystem.ConstMessages.STUDENT_NOT_FOUND;
import static com.leverx.learningmanagementsystem.ConstMessages.WELCOME_TO_THE_COURSE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.learningmanagementsystem.course.CourseRepository;
import com.leverx.learningmanagementsystem.mail.impl.DynamicMailServiceImpl;
import com.leverx.learningmanagementsystem.mapper.StudentMapper;
import com.leverx.learningmanagementsystem.student.StudentRepository;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentDto;
import com.leverx.learningmanagementsystem.student.dto.UpdateStudentDto;
import com.leverx.learningmanagementsystem.student.service.StudentService;
import jakarta.mail.MessagingException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;
  private final CourseRepository courseRepository;
  private final DynamicMailServiceImpl mailService;

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
  public void enrollToCourse(UUID studentId, UUID courseId) throws MessagingException {
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
      var currentPaid = getCourse.getCoinsPaid();
      getCourse.setCoinsPaid(currentPaid.add(getCourse.getPrice()));
      getStudent.getCourses().add(getCourse);
      getStudent.setCoins(getStudent.getCoins().subtract(getCourse.getPrice()));
      courseRepository.save(getCourse);
      studentRepository.save(getStudent);
      mailService.sendEmail(new String[]{getStudent.getEmail()},
          FROM_MAIL,
          String.format(WELCOME_TO_THE_COURSE, getCourse.getTitle()),
          COURSE_ENROLLED_SUBJECT);
    } else {
      throw new ResponseStatusException(BAD_REQUEST, NOT_ENOUGH_COINS);
    }
  }

  @Transactional
  @Override
  public StudentDto createStudent(CreateStudentDto studentDto) {
    var newUser = studentMapper.toEntity(studentDto);
    newUser = studentRepository.save(newUser);
    return studentMapper.toDto(newUser);
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
  public StudentDto updateStudent(UUID id, UpdateStudentDto studentDto) {
    var currentStudent = studentRepository.findById(id)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, String.format(STUDENT_NOT_FOUND, id)));
    studentMapper.update(studentDto, currentStudent);
    studentRepository.save(currentStudent);
    return studentMapper.toDto(currentStudent);
  }
}
