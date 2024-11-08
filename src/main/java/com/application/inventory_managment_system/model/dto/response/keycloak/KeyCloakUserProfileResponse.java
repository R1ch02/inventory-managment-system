package com.application.inventory_managment_system.model.dto.response.keycloak;

import java.util.UUID;

import lombok.Data;

@Data
public class KeyCloakUserProfileResponse {
    
    private UUID id;

    private String username;

    private String fisrtName;

    private String lastName;

    private String email;
}
