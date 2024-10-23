package com.application.inventory_managment_system.controllers;


import com.application.inventory_managment_system.mappers.UserMapper;
import com.application.inventory_managment_system.model.dto.request.UserRequest;
import com.application.inventory_managment_system.model.dto.response.MessageResponse;
import com.application.inventory_managment_system.model.dto.response.UserResponse;
import com.application.inventory_managment_system.model.view.UserView;
import com.application.inventory_managment_system.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
@Tag(name = "Контроллер профиля пользователя", description = "API по работе с профилем пользователя")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    @Operation(
        summary = "Вывод всех пользователей",
        description = "GET API запрос на постраничный вывод пользователей",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK")
        }
    )
    public ResponseEntity<Page<UserResponse>> findAllUsers(@ParameterObject Pageable pageable){

        return ResponseEntity.ok(
            userService.findAllUsers(pageable).map(user -> userMapper.toUserResponse(user))
        );
    }
    
    //В get запросе использовать @RequestParam 
    
    @GetMapping("/user")
    @Operation(
        summary = "Вывод пользователя",
        description = "GET API запрос на получение пользователя по ID",
        responses = {
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\"message\": \"Error\", \"error\": \"Пользователь не найден\"}")
                })
            }),
            @ApiResponse(responseCode = "200", description = "Пользователь найден")
        }
    )
    public ResponseEntity<UserResponse> findById(@RequestParam @Validated Long id){
        return ResponseEntity.ok(userMapper.toUserResponse
         (userService.getUserById(id)));
    }

    //Не в get запросах использовать @RequestBody и @PathVariable
    @PostMapping(value = "/user/add", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Регистрация пользователя",
        description = "POST API запрос на регистрацию ползователя",
        responses = {
            @ApiResponse(responseCode = "201", description = "Пользователь зарегестрирован")
        }
    )
    public ResponseEntity<MessageResponse> createUser(@RequestBody @Validated @JsonView(UserView.CreateUser.class) UserRequest userRequest) {
        
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new MessageResponse(
            userService.addUser(
                userMapper.toUser(userRequest)
            )
        ));
    }


    
    @PutMapping("user/update/data/{id}")
    @Operation(
        summary = "Редактирование пользователя",
        description = "PUT API запрос на редактирование ползователя",
        responses = {
            @ApiResponse(responseCode = "202", description = "Пользователь отредактирован")
        }
    )
    public ResponseEntity<UserResponse> updateUserDataById(@PathVariable Long id, @RequestBody @Validated @JsonView(UserView.UpdateUser.class) UserRequest userRequest) {
        
        
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(
            userMapper.toUserResponse(
                    userService.updateUserData(id,userRequest)
                )
            );
    }

    //TODO посмотреть groups Validation. Можно использовать те же самые классы, что и для JsonView
    @PutMapping("user/update/password/{id}")
    @Operation(
        summary = "Редактирование пароля пользователя",
        description = "PUT API запрос на редактирование ползователя",
        responses = {
            @ApiResponse(responseCode = "202", description = "Пароль пользователя отредактирован")
        }
    )
    public ResponseEntity<UserResponse> updateUserPassword(@PathVariable Long id, @RequestBody @JsonView(UserView.UpdateUserPassword.class) @Validated UserRequest userRequest) {
        
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(userMapper.toUserResponse(
                    userService.updateUserData(id, userRequest)
                )
            );
    }


  
    @DeleteMapping("/user/delete/{id}")
    @Operation(
        summary = "Удаление пользователя",
        description = "DELETE API запрос на удаление пользователя по id",
        responses = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален")
    })
    public ResponseEntity<String> deleteUser(@PathVariable @Validated Long id){

        
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(userService.deleteUserById(id));
    }




}
