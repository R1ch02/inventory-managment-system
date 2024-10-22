package com.application.inventory_managment_system.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.application.inventory_managment_system.model.dto.response.UserResponse;
import com.application.inventory_managment_system.model.entities.User;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
    )
public interface UserMapper{

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

}
