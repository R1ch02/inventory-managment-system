package com.application.inventory_managment_system.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO пользователя на ответ")
@Getter
@Setter
public class UserResponse {
    
    @Schema(description = "Id пользователя", example = "3")
    private Long id;

    @Schema(description = "Логин пользователя", example = "Alexey1999")
    private String username;

    @Schema(description = "Email пользователя", example = "example@example.com")
    private String email;
    

}
