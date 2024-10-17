package com.application.inventory_managment_system.mappers;


import com.application.inventory_managment_system.entities.User;
import com.application.inventory_managment_system.entities.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

}
