package com.application.inventory_managment_system.model.dto.response;

import java.util.List;
import java.util.UUID;

import com.application.inventory_managment_system.model.entities.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO пользователя на ответ")
@Getter
@Setter
public class UserResponse {
    
    @Schema(description = "Id пользователя", example = " ")
    private UUID id;

    @Schema(description = "Логин пользователя", example = "Alexey1999")
    private String username;

    @Schema(description = "Email пользователя", example = "example@example.com")
    private String email;

    @Schema(description = "Список продуктов")
    private List<Product> products;
    

}
