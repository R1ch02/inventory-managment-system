package com.application.inventory_managment_system.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.application.inventory_managment_system.model.dto.request.UserRequest;
import com.application.inventory_managment_system.model.dto.request.keycloak.KeyCloakUserRepresentation;
import com.application.inventory_managment_system.model.dto.request.keycloak.KeyCloakUserRepresentation.Credentials;
import com.application.inventory_managment_system.model.dto.response.UserResponse;
import com.application.inventory_managment_system.model.entities.User;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE

)
public interface UserMapper{

    UserResponse toUserResponse(User user);

    User toUser(UserRequest userRequest);

    List<UserResponse> toUserResponseList(List<User> users);


    void updateUserFromDto(UserRequest userRequest, @MappingTarget User updatedUser);

    @Mapping(target = "credentials", source = "password", qualifiedByName = "getCredentials")
    KeyCloakUserRepresentation userRequestToKeyCloakUserRepresentation(UserRequest userRequest);

    @Named("getCredentials")
    default List<Credentials> getCredentials(String password){
        return List.of(new Credentials(password));
    }


}
