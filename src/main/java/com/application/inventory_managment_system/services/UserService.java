package com.application.inventory_managment_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.inventory_managment_system.exceptions.ApiServiceException;
import com.application.inventory_managment_system.mappers.UserMapper;
import com.application.inventory_managment_system.model.dto.request.UserRequest;
import com.application.inventory_managment_system.model.entities.User;
import com.application.inventory_managment_system.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    

    public User getUserById(Long id){ 
        return userRepository.findById(id).orElseThrow(() -> new ApiServiceException("Не найден пользователь с id - " + id, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public User addUser(User user){
        return userRepository.save(user);
    }

    //TODO Добавить в репозиторий метод deleteById, который возвращал бы boolean
    @Transactional
    public String deleteUserById(Long id){
        //Начало костыля
        User user = userRepository.findById(id).orElseThrow(() -> new ApiServiceException("Не найден пользователь с id - " + id, HttpStatus.NOT_FOUND));
        //Конец костыля
        userRepository.deleteById(id);
        return "Пользователь с id - " + id + " удален";
    }

    public Page<User> findAllUsers(Pageable pageable){

       return userRepository.findAll(pageable);
    }

    @Transactional
    public User updateUserData(Long id, UserRequest userRequest) {
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new ApiServiceException("Пользователь не найден", HttpStatus.NOT_FOUND));
        userMapper.updateUserFromDto(userRequest,updatedUser);
        
        return userRepository.save(updatedUser);
    }






}
