package com.leverx.Mapper;

import com.leverx.learning_management_system.course.Course;
import com.leverx.learning_management_system.course.dto.CourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

  @Mappings({
      @Mapping(source = "title", target = "title"),
      @Mapping(source = "description", target = "description"),
      @Mapping(source = "price", target = "price"),
      @Mapping(source = "coinsPaid", target = "coinsPaid"), })
  CourseDto toDto(Course entity);
}
