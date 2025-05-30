package com.leverx.learningmanagementsystem.mapper;

import com.leverx.learningmanagementsystem.course.Course;
import com.leverx.learningmanagementsystem.course.dto.CourseDto;
import com.leverx.learningmanagementsystem.course.dto.DetailedCourseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

  @Mappings({
      @Mapping(source = "title", target = "title"),
      @Mapping(source = "description", target = "description"),
      @Mapping(source = "price", target = "price"),})
  @BeanMapping(ignoreByDefault = true)
  CourseDto toDto(Course entity);

  @Mappings({
      @Mapping(source = "title", target = "title"),
      @Mapping(source = "description", target = "description"),
      @Mapping(source = "price", target = "price"),
      @Mapping(source = "settings", target = "settings"),
      @Mapping(source = "lessons", target = "lessons"),
      @Mapping(source = "students", target = "students"),})
  @BeanMapping(ignoreByDefault = true)
  DetailedCourseDto toDetailedDto(Course entity);
}


