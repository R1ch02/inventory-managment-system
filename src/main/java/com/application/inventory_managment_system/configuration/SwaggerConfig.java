package com.application.inventory_managment_system.configuration;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(
    name = "OAuthUser",
    description = "OAuth-клиент для пользователей", 
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        authorizationCode = @OAuthFlow(
            authorizationUrl = "https://dev-id.acgnn.ru/realms/ims-realm/protocol/openid-connect/auth",
            tokenUrl = "https://dev-id.acgnn.ru/realms/ims-realm/protocol/openid-connect/token",
            refreshUrl = "https://dev-id.acgnn.ru/realms/ims-realm/protocol/openid-connect/token"
        )
    )
)
@Configuration
public class SwaggerConfig {

}
