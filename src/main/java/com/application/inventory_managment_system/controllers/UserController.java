package com.application.inventory_managment_system.controllers;


import com.application.inventory_managment_system.entities.User;
import com.application.inventory_managment_system.entities.dto.UserResponse;
import com.application.inventory_managment_system.mappers.UserMapper;
import com.application.inventory_managment_system.services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

//TODO Добавить валидацию
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> findAllUsers(@RequestParam(required = false, defaultValue = "0") int page, 
                                                    @RequestParam(required = false, defaultValue = "10") int size ){
        List<User> users = userService.findAllUsers(PageRequest.of(page, size));
        List<UserResponse> userResponsesList = userMapper.toUserResponseList(users);
        return ResponseEntity.ok(userResponsesList);
    }
    
    //В get запросе использовать @RequestParam 
    @GetMapping("/user")
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

    @PutMapping("user/update/{id}")
    public User putMethodName(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id,user);
        
        return userService.getUserById(id);
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id){
        if (id == null) {
            return "Id не может быть null";
        }
        userService.deleteUserById(id);
        return "Deleted user with id - " + id;
    }




}
