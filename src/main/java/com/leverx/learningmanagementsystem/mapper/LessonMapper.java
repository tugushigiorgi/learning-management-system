package com.leverx.learningmanagementsystem.mapper;

import com.leverx.learningmanagementsystem.lesson.Lesson;
import com.leverx.learningmanagementsystem.lesson.dto.LessonDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LessonMapper {

  @Mappings({
      @Mapping(source = "id", target = "id"),
      @Mapping(source = "title", target = "title"),
      @Mapping(source = "duration", target = "duration"), })
  @BeanMapping(ignoreByDefault = true)
  LessonDto toDto(Lesson entity);
}
