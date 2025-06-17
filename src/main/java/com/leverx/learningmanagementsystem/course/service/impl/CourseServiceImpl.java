package com.leverx.learningmanagementsystem.course.service.impl;

import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NEWS;
import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NEWS_SUBJECT;
import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_STARTING_TOMORROW;
import static com.leverx.learningmanagementsystem.ConstMessages.FROM_MAIL;
import static com.leverx.learningmanagementsystem.ConstMessages.STUDENTS_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.util.CollectionUtils.isEmpty;

import com.leverx.learningmanagementsystem.course.CourseRepository;
import com.leverx.learningmanagementsystem.course.dto.CourseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.dto.DetailedCourseDto;
import com.leverx.learningmanagementsystem.course.dto.UpdateCourseDto;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.mail.impl.DynamicMailServiceImpl;
import com.leverx.learningmanagementsystem.mapper.CourseMapper;
import com.leverx.learningmanagementsystem.mapper.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.student.Student;
import jakarta.mail.MessagingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final DynamicMailServiceImpl mailTrapImp;
  private final CourseSettingsMapper courseSettingsMapper;

  @Override
  @Transactional(readOnly = true)
  public void printAllCoursesByStartDateBetween(LocalDateTime start, LocalDateTime end) {
    var courses = courseRepository.findAllByStartDateBetween(start, end);
    System.out.printf(COURSE_STARTING_TOMORROW, courses.size());
    courses.forEach(c -> System.out.println("- " + c.getTitle()));
  }

  @Override
  @Transactional(readOnly = true)
  public List<CourseDto> getMostPopularCourses() {
    return courseRepository.findTop5MostPopular()
        .stream()
        .map(courseMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public CourseDto createCourse(CreateCourseDto courseDto) {
    var courseSettings = courseSettingsMapper.toEntity(courseDto);
    var newCourse = courseMapper.toEntity(courseDto);
    newCourse.setCoinsPaid(BigDecimal.ZERO);
    newCourse.setSettings(courseSettings);
    var newcourse = courseRepository.save(newCourse);
    return courseMapper.toDto(newcourse);
  }

  @Override
  @Transactional(readOnly = true)
  public CourseDto getCourseById(UUID id) {
    return courseRepository.findById(id)
        .map(courseMapper::toDto)
        .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CourseDto> getAllCourses() {
    return courseRepository.findAll()
        .stream()
        .map(courseMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    var course = courseRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, id)));
    if (!isEmpty(course.getStudents())) {
      course.getStudents()
          .forEach(student -> student.getCourses().remove(course));
    }
    courseRepository.delete(course);
  }

  @Override
  @Transactional
  public CourseDto updateCourse(UUID id, UpdateCourseDto courseDto) {
    var currentCourse = courseRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, id)));
    courseMapper.update(courseDto, currentCourse);
    courseRepository.save(currentCourse);
    return courseMapper.toDto(currentCourse);
  }

  @Override
  @Transactional(readOnly = true)
  public DetailedCourseDto getDetailedCourseById(UUID id) {
    return courseRepository.findById(id)
        .map(courseMapper::toDetailedDto)
        .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public void sendMailToEnrolledStudents(UUID courseId) throws MessagingException {
    var course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, courseId)));
    if (isEmpty(course.getStudents())) {
      throw new ResponseStatusException(NOT_FOUND, STUDENTS_NOT_FOUND);
    }
    var studentsEmails = course.getStudents()
        .stream()
        .map(Student::getEmail)
        .toArray(String[]::new);
    mailTrapImp.sendEmail(studentsEmails, FROM_MAIL, COURSE_NEWS_SUBJECT, COURSE_NEWS);
  }
}