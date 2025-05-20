package com.leverx.learning_management_system.Mapper;

import com.leverx.learning_management_system.lesson.Lesson;
import com.leverx.learning_management_system.lesson.dto.LessonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface LessonMapper {

  @Mappings({
      @Mapping(source = "title", target = "title"),
      @Mapping(source = "duration", target = "duration"), })
  LessonDto toDto(Lesson entity);
}
