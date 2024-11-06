package com.application.inventory_managment_system.model.dto.response.keycloak;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "DTO по работе с ключами доступа")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KeyCloakJwtResponse {


    @Schema(description = "Ключ доступа")
    private String accessToken;


    @Schema(description = "Время жизни ключа доступа")
    private Integer expiresIn;

    @Schema(description = "Время жизни ключа обновления")
    private Integer refreshExpiresIn;

    @Schema(description = "Ключ обновления")
    private String refreshToken;

    @Schema(description = "Тип токена")
    private String tokenType;

    @Schema(description = "Идентифицирующий ключ")
    private String idToken;

    @Schema(description = "Спец. параметр от Keycloak о сессии пользователя")
    private String sessionState;

    @Schema(description = "Область видимости")
    private String scope;

}
