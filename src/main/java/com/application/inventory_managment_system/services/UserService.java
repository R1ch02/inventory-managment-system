package com.application.inventory_managment_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.inventory_managment_system.exceptions.ApiServiceException;
import com.application.inventory_managment_system.model.entities.User;
import com.application.inventory_managment_system.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    public User getUserById(Long id){
        if (id == null) {
            throw new ApiServiceException("User_id не может быть null", HttpStatus.BAD_REQUEST);
        }
        
        return userRepository.findById(id).orElseThrow(() -> new ApiServiceException("Пользователь не найден", HttpStatus.NOT_FOUND));
    }

    public void addUser(User user){
        if (user == null) {
            
           throw new ApiServiceException("User не может быть null", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);



    }

    public void deleteUserById(Long id){
        if (id == null) {
            throw new ApiServiceException("User_Id не может быть null", HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteById(id);
    }

    public Page<User> findAllUsers(Pageable pageable){

       return userRepository.findAll(pageable);
    }






}
