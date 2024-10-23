package com.application.inventory_managment_system.model.dto.request;

import org.hibernate.validator.constraints.Length;

import com.application.inventory_managment_system.model.view.UserView;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "DTO пользователя на запрос для изменения данных")
public class UserRequest {

    @NotBlank
    @Length(min = 3, max = 30)
    @Schema(description = "Логин пользователя", example = "Alexey1999")
    @JsonView({ UserView.CreateUser.class, UserView.UpdateUser.class })
    private String username;

    @NotBlank
    @Email
    @Length(max = 30)
    @Schema(description = "Электронный адрес пользователя", example = "example@ex.ru")
    @JsonView({ UserView.CreateUser.class, UserView.UpdateUser.class })
    private String email;

    @NotBlank
    @Length(min = 6, max = 20)
    @JsonView({UserView.CreateUser.class, UserView.UpdateUserPassword.class})
    private String password;



}
