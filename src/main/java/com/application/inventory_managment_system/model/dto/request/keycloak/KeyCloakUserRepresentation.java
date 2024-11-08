package com.application.inventory_managment_system.model.dto.request.keycloak;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyCloakUserRepresentation {

    private String email;
    private String username;
    private Boolean enabled;
    private Boolean emailVerified;

    private List<Credentials> credentials;

    @Getter
    public static class Credentials {

        public Credentials(String password) {
            this.value = password;
            this.type = "password";
            this.temporary = false;
        }

        private String type;

        private String value;

        private boolean temporary; 
    
    }

}
