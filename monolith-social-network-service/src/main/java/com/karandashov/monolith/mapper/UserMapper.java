package com.karandashov.monolith.mapper;

import com.karandashov.monolith.dto.User;
import com.karandashov.monolith.dto.request.RegisterRequest;
import com.karandashov.monolith.entity.UserEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "birthDate", source = "birthdate", qualifiedByName = "toZonedDateTime")
    UserEntity toEntity(RegisterRequest request);

    @Named("toZonedDateTime")
    default ZonedDateTime toZonedDateTime(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault());
    }

    User toDto(UserEntity userEntity);
}
