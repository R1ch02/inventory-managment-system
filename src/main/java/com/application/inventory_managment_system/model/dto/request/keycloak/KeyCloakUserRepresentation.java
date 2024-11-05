package com.application.inventory_managment_system.model.dto.request.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyCloakUserRepresentation {

    private String email;
    private String firstName;
    private Boolean enabled;
    private Boolean emailVerified;

}
