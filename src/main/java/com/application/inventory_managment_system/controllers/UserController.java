package com.application.inventory_managment_system.controllers;


import com.application.inventory_managment_system.entities.User;
import com.application.inventory_managment_system.entities.dto.UserResponse;
import com.application.inventory_managment_system.mappers.UserMapper;
import com.application.inventory_managment_system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(){
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponsesList = userMapper.toUserResponseList(users);
        return ResponseEntity.ok(userResponsesList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id){
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok(userMapper.toUserResponse(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
