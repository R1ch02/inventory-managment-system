package com.application.inventory_managment_system.model.dto.response;

import java.util.List;

import com.application.inventory_managment_system.model.entities.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO товара на ответ")
@Getter
@Setter
public class ProductResponse {


    @Schema(description = "Id товара", example = "42")
    private Long id;

    @Schema(description = "Название товара", example = "Круасан")
    private String name;

    @Schema(description = "Описание товара", example = "Круасан 7 DAYS с клубничной начинкой")
    private String description;

    @Schema(description = "Цена товара", example = "79.99")
    private double price;

    @Schema(description = "Количество товара в наличии", example = "20")
    private int quantity;

    @Schema(description = "Список пользователей, покупающих этот продукт")
    private List<User> users;


    

} 
