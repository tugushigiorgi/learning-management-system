package com.leverx.learning_management_system.course.Service.imp;

import static com.leverx.learning_management_system.ConstMessages.COURSE_NOT_FOUND;
import static com.leverx.learning_management_system.ConstMessages.STUDENT_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.Mapper.CourseMapper;
import com.leverx.learning_management_system.course.Course;
import com.leverx.learning_management_system.course.CourseRepository;
import com.leverx.learning_management_system.course.Service.CourseService;
import com.leverx.learning_management_system.course.dto.CourseDto;
import com.leverx.learning_management_system.course.dto.CreateCourseDto;
import com.leverx.learning_management_system.course.dto.UpdateCourseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
public class CourseServiceImp implements CourseService {
  private final CourseRepository courseRepository;
  private final CourseMapper studentMapper;

  @Override
  @Transactional
  public void createCourse(CreateCourseDto courseDto) {
    var newCourse = Course.builder()
        .price(courseDto.getPrice())
        .title(courseDto.getTitle())
        .description(courseDto.getDescription())
        .build();
    courseRepository.save(newCourse);
  }

  @Override
  @Transactional(readOnly = true)
  public CourseDto getCourseById(UUID id) {
    return courseRepository.findById(id)
        .map(studentMapper::toDto)
        .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CourseDto> getAllCourses() {
    return courseRepository.findAll().stream()
        .map(studentMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    var getCourse = courseRepository.findById(id)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, COURSE_NOT_FOUND + id)
        );
    courseRepository.delete(getCourse);
  }

  @Override
  @Transactional
  public void updateCourse(UpdateCourseDto courseDto) {
    var currentCourse = courseRepository.findById(courseDto.getId())
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, COURSE_NOT_FOUND + courseDto.getId())
        );
    if (!courseDto.getTitle().equals(currentCourse.getTitle())) {
      currentCourse.setTitle(courseDto.getTitle());
    }

    if (!courseDto.getDescription().equals(currentCourse.getDescription())) {
      currentCourse.setDescription(courseDto.getDescription());
    }

    if (!courseDto.getPrice().equals(currentCourse.getPrice())) {
      currentCourse.setPrice(courseDto.getPrice());
    }

    courseRepository.save(currentCourse);
  }
}
