package com.application.inventory_managment_system.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.application.inventory_managment_system.exceptions.ApiServiceException;
import com.application.inventory_managment_system.mappers.UserMapper;
import com.application.inventory_managment_system.model.dto.request.UserRequest;
import com.application.inventory_managment_system.model.dto.response.keycloak.KeyCloakUserInfoResponse;
import com.application.inventory_managment_system.model.entities.Product;
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
    private final RestService restService;

    public User findUserByUsername(String username){ 
        return userRepository.findByUsername(username).orElseThrow(() -> new ApiServiceException("Не найден пользователь с username = " + username, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public User addUser(User user){
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ApiServiceException("Пользователь с таким логином уже существует", HttpStatus.CONFLICT);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            
            throw new ApiServiceException("Пользователь с таким email уже существует", HttpStatus.CONFLICT);
        }
        return userRepository.save(user);
    }

    //TODO Добавить в репозиторий метод deleteById, который возвращал бы boolean
    @Transactional
    public Boolean deleteUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            for (Product product : user.get().getProducts()) {
                product.getUsers().remove(user);
            }
            userRepository.deleteByUsername(username);
            return true;
        } else {
            throw new ApiServiceException("Не найден пользователь с username = " + username, HttpStatus.NOT_FOUND);
        }
    }

    public Page<User> findAllUsers(Pageable pageable){
       return userRepository.findAll(pageable);
    }

    @Transactional
    public User updateUserData(UserRequest userRequest) {
        
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ApiServiceException("Пользователь с таким логином уже существует", HttpStatus.CONFLICT);
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ApiServiceException("Пользователь с таким email уже существует", HttpStatus.CONFLICT);
        }

        User updatedUser = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(() -> new ApiServiceException("Не найден пользователь с username = " + userRequest.getUsername(), HttpStatus.NOT_FOUND));

        userMapper.updateUserFromDto(userRequest,updatedUser);
        
        return userRepository.save(updatedUser);
    }

    @Transactional
    public User getUserFromSecurityContext(){
        JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        log.info("{}", token.getToken().getTokenValue());
        UUID userId = UUID.fromString(((Jwt) token.getPrincipal()).getSubject());
        if (userRepository.existsById(userId)) {
            throw new ApiServiceException("Пользователь уже существует", HttpStatus.CONFLICT);
        } 
        KeyCloakUserInfoResponse keyCloakUserInfoResponse = restService.getUserInfo(token.getToken().getTokenValue()).getBody();
        log.info("{}", keyCloakUserInfoResponse.getEmail());
        User user = new User();
        user.setId(userId);
        user.setEmail(token.getToken().getClaim("email"));
        user.setUsername(token.getToken().getClaim("preferred_username"));
        user.setProducts(new ArrayList<>());
        user.setLastUpDateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    // @Transactional 
    // public User persistOrUpdateUser(Jwt jwt){
    //     UUID userId = UUID.fromString(jwt.getSubject());
        
    //     User user = userRepository.findById(userId).orElseGet(
    //         () -> 

    //     )

    //}
    

    // @Transactional
    // public User updateUserFromSecurityContext(){
    //     JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    //     UUID userId = UUID.fromString(((Jwt) token.getPrincipal()).getSubject());

    // }



}
