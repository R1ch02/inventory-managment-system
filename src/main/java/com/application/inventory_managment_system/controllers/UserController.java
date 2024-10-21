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
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> findAll(@RequestParam(required = false, defaultValue = "0") int page, 
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
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ResponseEntity.ok(userMapper.toUserResponse(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Не в get запросах использовать @RequestBody и @PathVariable
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public Optional<User> createUser(@RequestBody User user) {
        if (user == null) {
            System.out.println("user не можеть быть null");
            return null;
        }

        userService.addUser(user);
        return userService.getUserById(user.getId());
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        if (id == null) {
            return "Id не может быть null";
        }
        userService.deleteUserById(id);
        return "Deleted user with id - " + id;
    }




}
