package com.leverx.learningmanagementsystem.course.service.imp;

import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NEWS;
import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NEWS_SUBJECT;
import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learningmanagementsystem.ConstMessages.COURSE_STARTING_TOMORROW;
import static com.leverx.learningmanagementsystem.ConstMessages.FROM_MAIL;
import static com.leverx.learningmanagementsystem.ConstMessages.STUDENTS_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.util.CollectionUtils.isEmpty;

import com.leverx.learningmanagementsystem.course.Course;
import com.leverx.learningmanagementsystem.course.CourseRepository;
import com.leverx.learningmanagementsystem.course.dto.CourseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.dto.DetailedCourseDto;
import com.leverx.learningmanagementsystem.course.dto.UpdateCourseDto;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.courseSettings.CourseSettings;
import com.leverx.learningmanagementsystem.mailtrap.imp.MailTrapImp;
import com.leverx.learningmanagementsystem.mapper.CourseMapper;
import io.mailtrap.model.request.emails.Address;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class CourseServiceImp implements CourseService {
  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final MailTrapImp mailTrapImp;

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
    var courseSettings = CourseSettings.builder()
        .startDate(courseDto.getStartDate())
        .endDate(courseDto.getEndDate())
        .isPublic(courseDto.getIsPublic())
        .build();
    var newCourse = Course.builder()
        .price(courseDto.getPrice())
        .title(courseDto.getTitle())
        .description(courseDto.getDescription())
        .settings(courseSettings)
        .build();
    var savedCourse=courseRepository.save(newCourse);
    return courseMapper.toDto(savedCourse);
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
    var getCourse = courseRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, id)));
    courseRepository.delete(getCourse);
  }

  @Override
  @Transactional
  public CourseDto updateCourse(UpdateCourseDto courseDto) {
    var currentCourse = courseRepository.findById(courseDto.getId())
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, courseDto.getId())));
    if (!courseDto.getTitle().equals(currentCourse.getTitle())) {
      currentCourse.setTitle(courseDto.getTitle());
    }
    if (!courseDto.getDescription().equals(currentCourse.getDescription())) {
      currentCourse.setDescription(courseDto.getDescription());
    }
    if (!courseDto.getPrice().equals(currentCourse.getPrice())) {
      currentCourse.setPrice(courseDto.getPrice());
    }
    var courseSettings = currentCourse.getSettings();
    if (!courseDto.getStartDate().equals(courseSettings.getStartDate())) {
      courseSettings.setStartDate(courseDto.getStartDate());
    }
    if (!courseDto.getEndDate().equals(courseSettings.getEndDate())) {
      courseSettings.setEndDate(courseDto.getEndDate());
    }
    if (!courseDto.getIsPublic().equals(courseSettings.getIsPublic())) {
      courseSettings.setIsPublic(courseDto.getIsPublic());
    }
    var updatedCourse=courseRepository.save(currentCourse);
    return courseMapper.toDto(updatedCourse);
  }

  @Override
  @Transactional(readOnly = true)
  public DetailedCourseDto getDetailedCourseById(UUID id) {
    return courseRepository.findById(id)
        .map(courseMapper::toDetailedDto)
        .orElse(null);
  }

  @Override
  public void sendMailToEnrolledStudents(UUID courseId) {
    var course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format(COURSE_NOT_FOUND, courseId)));
    if (isEmpty(course.getStudents())) {
      throw new ResponseStatusException(NOT_FOUND, STUDENTS_NOT_FOUND);
    }
    var studentsEmails = course.getStudents()
        .stream()
        .map(student -> new Address(student.getEmail()))
        .toList();
    mailTrapImp.sendEmail(studentsEmails, FROM_MAIL, COURSE_NEWS_SUBJECT, COURSE_NEWS);
  }
}
