package com.leverx.Mapper;

import com.leverx.learning_management_system.student.Student;
import com.leverx.learning_management_system.student.dto.StudentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {
  @Mappings({
      @Mapping(source = "firstName", target = "firstName"),
      @Mapping(source = "lastName", target = "lastName"),
      @Mapping(source = "email", target = "email"),
      @Mapping(source = "dateOfBirth", target = "dateOfBirth"),
      @Mapping(source = "coins", target = "coins"), })
  StudentDto toDto(Student entity);


}
