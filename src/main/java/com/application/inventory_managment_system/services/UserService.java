package com.application.inventory_managment_system.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.application.inventory_managment_system.entities.User;
import com.application.inventory_managment_system.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    //TODO Сделать кастомный exception
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public void addUser(User user){
        if (user == null) {
            //log.error("User can not be null", null);
            return;
        }
        userRepository.save(user);



    }

    public void deleteUserById(Long id){
        if (id == null) {
            return;
        }
        userRepository.deleteById(id);
    }

    public List<User> findAllUsers(PageRequest pageRequest){

        return userRepository.findAllByPageRequest(pageRequest);
    }



}
