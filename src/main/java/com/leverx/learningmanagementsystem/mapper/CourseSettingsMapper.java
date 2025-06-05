package com.leverx.learningmanagementsystem.mapper;

import com.leverx.learningmanagementsystem.course.dto.CourseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.coursesettings.CourseSettings;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseSettingsMapper {

  @Mappings({
      @Mapping(source = "startDate", target = "startDate"),
      @Mapping(source = "endDate", target = "endDate"),
      @Mapping(source = "isPublic", target = "isPublic")})
  @BeanMapping(ignoreByDefault = true)
  CourseSettings toEntity(CreateCourseDto courseDto);
}
