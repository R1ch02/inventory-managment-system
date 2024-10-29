package com.application.inventory_managment_system.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.application.inventory_managment_system.model.dto.request.UserRequest;
import com.application.inventory_managment_system.model.dto.response.UserResponse;
import com.application.inventory_managment_system.model.entities.User;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE

)
public interface UserMapper{

    UserResponse toUserResponse(User user);

    User toUser(UserRequest userRequest);

    List<UserResponse> toUserResponseList(List<User> users);


    void updateUserFromDto(UserRequest userRequest, @MappingTarget User updatedUser);


}
