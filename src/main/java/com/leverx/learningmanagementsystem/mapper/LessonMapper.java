package com.leverx.learningmanagementsystem.mapper;

import com.leverx.learningmanagementsystem.lesson.Lesson;
import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.UpdateLessonDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LessonMapper {

  @Mappings({
      @Mapping(source = "id", target = "id"),
      @Mapping(source = "title", target = "title"),
      @Mapping(source = "duration", target = "duration"),})
  @BeanMapping(ignoreByDefault = true)
  LessonDto toDto(Lesson entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Lesson update(UpdateLessonDto dto, @MappingTarget Lesson entity);

  @Mappings({
      @Mapping(source = "title", target = "title"),
      @Mapping(source = "duration", target = "duration"),})
  @BeanMapping(ignoreByDefault = true)
  Lesson toEntity(CreateLessonDto dto);
}
