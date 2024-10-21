package com.application.inventory_managment_system.services;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.inventory_managment_system.entities.User;
import com.application.inventory_managment_system.exceptions.ApiServiceException;
import com.application.inventory_managment_system.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//TODO добавить валидацию
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

    public List<User> findAllUsers(PageRequest pageRequest){

        return userRepository.findAllByPageRequest(pageRequest);
    }

    //TODO стоит ли писать отдельный метод?
    public void updateUser(Long id, User user) {

        
    }





}
