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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @SecurityRequirement(name = "OAuthUser")
    @PreAuthorize("hasRole('realm_ims-admin')")
    @Operation(
        summary = "Вывод всех пользователей",
        description = "GET API запрос на постраничный вывод пользователей",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK")
        }
    )
    public ResponseEntity<Page<UserResponse>> findAllUsers(@ParameterObject Pageable pageable){

        userService.persistOrUpdateUser();

        return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.findAllUsers(pageable).map(user -> userMapper.toUserResponse(user))
        );
    }
    
    //В get запросе использовать @RequestParam 

    @GetMapping("/user/info")
    @PreAuthorize("hasRole('realm_ims-admin')")
    @SecurityRequirement(name = "OAuthUser")
    @Operation(
        summary = "Вывод пользователя",
        description = "GET API запрос на получение пользователя по ID",
        responses = {
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\"message\": \"Error\", \"error\": \"Пользователь не найден\"}")
                })
            }),
            @ApiResponse(responseCode = "400", description = "Введен невалидный id пользователя", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": {\r\n" + //
                                                "    \"findById.id\": \"должно быть больше 0\"\r\n" + //
                                                "  }\r\n" + //
                                                "}")
                })
            }),
            @ApiResponse(responseCode = "200", description = "Пользователь найден")
        }
    )
    public ResponseEntity<UserResponse> getUser(){
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(userMapper.toUserResponse(userService.persistOrUpdateUser()));
    }

    //Не в get запросах использовать @RequestBody и @PathVariable
    @PostMapping(value = "/user/add", consumes = "application/json", produces = "application/json")
    @SecurityRequirement(name = "OAuthUser")
    @PreAuthorize("hasRole('realm_ims-admin')")
    @Operation(
        summary = "Регистрация пользователя",
        description = "POST API запрос на регистрацию ползователя",
        responses = {
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = {
                @Content(examples = {
                    @ExampleObject(name = "Остутствуют поля", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": \"Некорректное тело запроса. Не обозначены обязательные поля\"\r\n" + //
                                                "}"),
                    @ExampleObject(name = "Не валидные поля", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": {\r\n" + //
                                                "    \"password\": \"длина должна составлять от 6 до 20\",\r\n" + //
                                                "    \"email\": \"должно иметь формат адреса электронной почты\"\r\n" + //
                                                "  }\r\n" + //
                                                "}")
                })
            }),
            @ApiResponse(responseCode = "409", description = "Пользователь с уникальным типом данных уже существует", content = {
                @Content(examples = {
                    @ExampleObject(name = "Логин занят", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": \"Пользователь с таким логином уже существует\"\r\n" + //
                                                "}"),
                    @ExampleObject(name = "Email занят", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": \"Пользователь с таким email уже существует\"\r\n" + //
                                                "}")
                })
            }),
            @ApiResponse(responseCode = "201", description = "Пользователь зарегестрирован", content = {
                @Content(examples = {
                    @ExampleObject(value =  "{\r\n" + //
                                                "  \"message\": {\r\n" + //
                                                "    \"id\": 3,\r\n" + //
                                                "    \"username\": \"Alexey199191\",\r\n" + //
                                                "    \"email\": \"example@ex.ru\",\r\n" + //
                                                "    \"password\": \"string\",\r\n" + //
                                                "    \"creationDateTime\": \"2024-10-24T16:35:37.075244\",\r\n" + //
                                                "    \"lastUpDateTime\": \"2024-10-24T16:35:37.075244\"\r\n" + //
                                                "  },\r\n" + //
                                                "  \"error\": null\r\n" + //
                                                "}")
                })
            })
        }
    )
    public ResponseEntity<MessageResponse> createUser(@RequestBody @Validated @JsonView(UserView.CreateUser.class) UserRequest userRequest) {

        userService.persistOrUpdateUser();

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new MessageResponse(
            userService.addUser(
                userMapper.toUser(userRequest)
            )
        ));
    }


    
    @PutMapping("user/update")
    @SecurityRequirement(name = "OAuthUser")
    @PreAuthorize("hasRole('ims-admin')")
    @Operation(
        summary = "Редактирование пользователя",
        description = "PUT API запрос на редактирование ползователя",
        responses = {
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = {
                @Content(examples = {
                    @ExampleObject(name = "Отсутствуют поля", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": \"Некорректное тело запроса. Не обозначены обязательные поля\"\r\n" + //
                                                "}"),
                    @ExampleObject(name = "Id > 0",value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": {\r\n" + //
                                                "    \"updateUserDataById.id\": \"должно быть больше 0\"\r\n" + //
                                                "  }\r\n" + //
                                                "}"),
                    @ExampleObject(name = "Не валидные поля", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": {\r\n" + //
                                                "    \"password\": \"длина должна составлять от 6 до 20\",\r\n" + //
                                                "    \"email\": \"должно иметь формат адреса электронной почты\"\r\n" + //
                                                "  }\r\n" + //
                                                "}")
                })
            }),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\"message\": \"Error\", \"error\": \"Пользователь не найден\"}")
                })
            }),
            @ApiResponse(responseCode = "409", description = "Пользователь с уникальным типом данных уже существует", content = {
                @Content(examples = {
                    @ExampleObject(name = "Логин занят", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": \"Пользователь с таким логином уже существует\"\r\n" + //
                                                "}"),
                    @ExampleObject(name = "Email занят", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": \"Пользователь с таким email уже существует\"\r\n" + //
                                                "}")
                })
            }),
            @ApiResponse(responseCode = "202", description = "Пользователь отредактирован", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\r\n" + //
                                                "  \"id\": 1,\r\n" + //
                                                "  \"username\": \"Alexey1999\",\r\n" + //
                                                "  \"email\": \"example@ex.ru\"\r\n" + //
                                                "}")
                })
            })
        }
    )
    public ResponseEntity<UserResponse> updateUserDataByName(@RequestBody @Validated(UserView.UpdateUser.class) @JsonView(UserView.UpdateUser.class) UserRequest userRequest) {
        
        userService.persistOrUpdateUser();
        
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(
            userMapper.toUserResponse(
                    userService.updateUserData(userRequest)
                )
            );
    }


  
    // @DeleteMapping("/user/delete/{username}")
    // @SecurityRequirement(name = "OAuthUser")
    // @PreAuthorize("hasRole('ims-admin')")
    // @Operation(
    //     summary = "Удаление пользователя",
    //     description = "DELETE API запрос на удаление пользователя по id",
    //     responses = {
    //         @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
    //             @Content(examples = {
    //                 @ExampleObject(value = "{\r\n" + //
    //                                             "  \"message\": \"Error\",\r\n" + //
    //                                             "  \"error\": \"Не найден пользователь с id = 2\"\r\n" + //
    //                                             "}")
    //             })
    //         }),
    //         @ApiResponse(responseCode = "400", description = "Введен невалидный id пользователя", content = {
    //             @Content(examples = {
    //                 @ExampleObject(value = "{\r\n" + //
    //                                             "  \"message\": \"Error\",\r\n" + //
    //                                             "  \"error\": {\r\n" + //
    //                                             "    \"deleteUserById.id\": \"Id должно быть больше 0\"\r\n" + //
    //                                             "  }\r\n" + //
    //                                             "}")
    //             })
    //         }),
    //         @ApiResponse(responseCode = "200", description = "Пользователь удален", content = {
    //             @Content(examples = {
    //                 @ExampleObject(value = "Пользователь с id '1' удален: true")
    //             })
    //         })
    // }
    // )
    // public ResponseEntity<String> deleteUser(@PathVariable @Parameter(description = "ID пользователя") @Validated @Positive String username){

    //     return ResponseEntity
    //     .status(HttpStatus.OK)
    //     .body("Пользователь с username '" + username + "' удален: " + userService.deleteUserByUsername(username));
    // }




}
