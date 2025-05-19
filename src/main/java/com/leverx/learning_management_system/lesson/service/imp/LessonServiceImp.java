package com.leverx.learning_management_system.lesson.service.imp;

import static com.leverx.learning_management_system.ConstMessages.LESSON_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leverx.learning_management_system.Mapper.LessonMapper;
import com.leverx.learning_management_system.lesson.Lesson;
import com.leverx.learning_management_system.lesson.LessonRepository;
import com.leverx.learning_management_system.lesson.dto.CreateLessonDto;
import com.leverx.learning_management_system.lesson.dto.LessonDto;
import com.leverx.learning_management_system.lesson.dto.UpdateLessonDto;
import com.leverx.learning_management_system.lesson.service.LessonService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LessonServiceImp implements LessonService {
  private final LessonRepository lessonRepository;
  private final LessonMapper lessonMapper;

  @Override
  @Transactional
  public void createLesson(CreateLessonDto lessonDto) {
    var newLesson = Lesson.builder()
        .title(lessonDto.getTitle())
        .duration(lessonDto.getDuration())
        .build();
    lessonRepository.save(newLesson);
  }

  @Override
  @Transactional(readOnly = true)
  public LessonDto getLessonById(UUID id) {
    return lessonRepository.findById(id)
        .map(lessonMapper::toDto)
        .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public List<LessonDto> getAllLessons() {
    return lessonRepository.findAll().stream()
        .map(lessonMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    var lesson = lessonRepository.findById(id)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, LESSON_NOT_FOUND + id)
        );
    lessonRepository.delete(lesson);
  }

  @Override
  @Transactional
  public void updateLessons(UpdateLessonDto lessonDto) {
    var currentLesson = lessonRepository.findById(lessonDto.getId())
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, LESSON_NOT_FOUND + lessonDto.getId())
        );
    if (!lessonDto.getTitle().equals(currentLesson.getTitle())) {
      currentLesson.setTitle(lessonDto.getTitle());
    }
    if (!lessonDto.getDuration().equals(currentLesson.getDuration())) {
      currentLesson.setDuration(lessonDto.getDuration());
    }
    lessonRepository.save(currentLesson);
  }
}
