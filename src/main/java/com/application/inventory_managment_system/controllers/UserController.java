package com.application.inventory_managment_system.controllers;


import com.application.inventory_managment_system.mappers.UserMapper;
import com.application.inventory_managment_system.model.dto.response.UserResponse;
import com.application.inventory_managment_system.model.entities.User;
import com.application.inventory_managment_system.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

//TODO Добавить валидацию
@RestController
@RequestMapping("/api")
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
                    @ExampleObject(value = "{\"message\": \"Error\", \"error\": \"Товар не найден\"}")
                })
            }),
            @ApiResponse(responseCode = "200", description = "OK")
        }
    )
    public ResponseEntity<UserResponse> findById(@RequestParam Long id){
        if (id == null) {
            System.out.println("Id не может быть null");
            return null;
        }
        return ResponseEntity.ok(userMapper.toUserResponse(userService.getUserById(id)));
    }

    //Не в get запросах использовать @RequestBody и @PathVariable
    @PostMapping(value = "/user/add", consumes = "application/json", produces = "application/json")
    public User createUser(@RequestBody User user) {
        if (user == null) {
            System.out.println("user не можеть быть null");
            return null;
        }

        userService.addUser(user);
        return userService.getUserById(user.getId());
    }

    //TODO добавить PUT запрос
    //TODO Jackson реализовать новые requestDTO и @JsonView
    //TODO Swagger написать документацию
    //TODO Validated настроить валидацию на уровне контроллера
    @PutMapping("user/update/{id}")
    public User putMethodName(@PathVariable Long id, @RequestBody User user) {
       
        
        return userService.getUserById(id);
    }

    //TODO Swagger написать документацию
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id){
        if (id == null) {
            return "Id не может быть null";
        }
        userService.deleteUserById(id);
        return "Deleted user with id - " + id;
    }




}
