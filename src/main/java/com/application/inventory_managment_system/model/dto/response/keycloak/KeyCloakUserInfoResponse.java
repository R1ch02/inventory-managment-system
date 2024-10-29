package com.application.inventory_managment_system.model.dto.response.keycloak;

import java.util.UUID;

import lombok.Data;

@Data
public class KeyCloakUserInfoResponse {

    private UUID id;

    private String username;
    
    private String email;

}
