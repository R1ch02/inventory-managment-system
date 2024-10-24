package com.application.inventory_managment_system.model.dto.request;

import org.hibernate.validator.constraints.Length;

import com.application.inventory_managment_system.model.view.ProductView;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
@Schema(description = "DTO товара на запрос")
public class ProductRequest {
    
    @NotBlank
    @Length(max = 30)
    @Schema(description = "Название товара", example = "Круасан")
    @JsonView({ProductView.CreateProduct.class, ProductView.UpdateProductData.class})
    private String name;

    @NotNull
    @Length(max = 200)
    @Schema(description = "Описание товара", example = "Круасан 7 DAYS с клубничной начинкой")
    @JsonView({ProductView.CreateProduct.class, ProductView.UpdateProductData.class})
    private String description;

    @NotNull
    @Positive
    @Schema(description = "Цена товара", example = "79.99")
    @JsonView({ProductView.CreateProduct.class, ProductView.UpdateProductData.class})
    private double price;
    
    @NotNull
    @PositiveOrZero
    @Schema(description = "Количество товара в наличии", example = "20")
    @JsonView({ProductView.CreateProduct.class, ProductView.UpdateProductData.class})
    private int quantity;

}
