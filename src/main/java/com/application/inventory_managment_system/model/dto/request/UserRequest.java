package com.application.inventory_managment_system.model.dto.request;

import org.hibernate.validator.constraints.Length;

import com.application.inventory_managment_system.model.view.UserView;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "DTO пользователя на запрос для изменения данных")
public class UserRequest {

    @NotBlank(groups = {UserView.CreateUser.class, UserView.UpdateUser.class})
    @Length(min = 3, max = 30, groups = {UserView.CreateUser.class, UserView.UpdateUser.class})
    @Schema(description = "Логин пользователя", example = "Alexey1999")
    @JsonView({ UserView.CreateUser.class, UserView.UpdateUser.class})
    private String username;

    @NotBlank(groups = {UserView.CreateUser.class, UserView.UpdateUser.class})
    @Email(groups = {UserView.CreateUser.class, UserView.UpdateUser.class})
    @Length(max = 30, groups = {UserView.CreateUser.class, UserView.UpdateUser.class})
    @Schema(description = "Электронный адрес пользователя", example = "example@ex.ru")
    @JsonView(UserView.CreateUser.class)
    private String email;
    
    @NotNull(groups = {UserView.CreateUser.class, UserView.UpdateUser.class})
    @Schema(description = "Действителен ли пользователь", example = "true")
    @JsonView({ UserView.CreateUser.class, UserView.UpdateUser.class })
    private boolean enabled;

    @NotNull(groups = {UserView.CreateUser.class, UserView.UpdateUser.class})
    @Schema(description = "Подтвержден ли email", example = "true")
    @JsonView({ UserView.CreateUser.class, UserView.UpdateUser.class })
    private boolean emailVerified;


    @NotBlank(groups = UserView.CreateUser.class)
    @Schema(description = "Пароль", example = "qwerty12345")
    @JsonView(UserView.CreateUser.class)
    private String password;

}
