package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.ActivateStatusRequestDto;
import com.bilgeadam.dto.request.UserCreateRequestDto;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserProfile fromCreateRequestToUser(UserCreateRequestDto dto);

    UserProfile fromRegisterModelToUserProfile(RegisterModel model);
    UserCreateRequestDto fromRegisterModelToUserCreateDto(RegisterModel model);

}
