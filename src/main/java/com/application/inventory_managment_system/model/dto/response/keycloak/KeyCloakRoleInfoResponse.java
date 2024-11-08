package com.application.inventory_managment_system.model.dto.response.keycloak;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;

@Data
public class KeyCloakRoleInfoResponse {

    private UUID id;
    private String name;
    private String description;
    private Boolean composite;
    private Boolean clientRole;
    private UUID containerId;
    private Attributes attributes;
    
    @Getter
    public static class Attributes {
        private String phoneNumber;
    }
}
