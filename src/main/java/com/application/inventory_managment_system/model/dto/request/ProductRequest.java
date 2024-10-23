package com.application.inventory_managment_system.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
@Schema(description = "DTO товара на запрос")
public class ProductRequest {
    
    @NotNull
    @Schema(description = "Название товара", example = "Круасан")
    private String name;

    @NotNull
    @Schema(description = "Описание товара", example = "Круасан 7 DAYS с клубничной начинкой")
    private String description;

    @NotNull
    @Positive
    @Schema(description = "Цена товара", example = "79.99")
    private double price;
    
    @NotNull
    @PositiveOrZero
    @Schema(description = "Количество товара в наличии", example = "20")
    private int quantity;

}
