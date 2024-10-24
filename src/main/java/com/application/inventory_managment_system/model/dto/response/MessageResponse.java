package com.application.inventory_managment_system.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO простого ответа")
public class MessageResponse {

    @Schema(description = "Сообщение ответа")
    public Object message;

    @Schema(description = "Если есть ошибка, то указывает тело ошибки, в случае успеха, это поле отсутствует")
    public Object error;

    public MessageResponse(Object message){
        this.message = message;
    }
    
}
