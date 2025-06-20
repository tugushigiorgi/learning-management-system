package com.leverx.learningmanagementsystem.mapper;

import com.leverx.learningmanagementsystem.student.Student;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {
  @Mappings({
      @Mapping(source = "id", target = "id"),
      @Mapping(source = "firstName", target = "firstName"),
      @Mapping(source = "lastName", target = "lastName"),
      @Mapping(source = "email", target = "email"),
      @Mapping(source = "dateOfBirth", target = "dateOfBirth"),
      @Mapping(source = "coins", target = "coins")})
  @BeanMapping(ignoreByDefault = true)
  StudentDto toDto(Student entity);

  @Mappings({
      @Mapping(source = "firstName", target = "firstName"),
      @Mapping(source = "lastName", target = "lastName"),
      @Mapping(source = "email", target = "email"),
      @Mapping(source = "dateOfBirth", target = "dateOfBirth")})
  @BeanMapping(ignoreByDefault = true)
  Student toEntity(CreateStudentDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Student update(StudentUpdateDto dto, @MappingTarget Student entity);
}
