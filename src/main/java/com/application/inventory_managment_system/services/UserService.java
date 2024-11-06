package com.application.inventory_managment_system.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Provider;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
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
import com.application.inventory_managment_system.model.dto.request.keycloak.KeyCloakUserRepresentation;
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
    private final OAuth2ClientProperties oauthProperties;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ApiServiceException("Не найден пользователь с username = " + username, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public User addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ApiServiceException("Пользователь с таким логином уже существует", HttpStatus.CONFLICT);
        }
        if (userRepository.existsByEmail(user.getEmail())) {

            throw new ApiServiceException("Пользователь с таким email уже существует", HttpStatus.CONFLICT);
        }
        
        UUID uuid;
        while (true) {
            uuid = UUID.randomUUID();
            if (!userRepository.existsById(uuid)) {
                user.setId(uuid);
                break;
            } 
        }

        restService.createUser(getServiceAccessToken(), getUserRepresentationByUser(user));

        return userRepository.save(user);
    }

    private KeyCloakUserRepresentation getUserRepresentationByUser(User user) {
        return KeyCloakUserRepresentation.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .emailVerified(true)
            .enabled(true)
            .build();
    }

    @Transactional
    public Boolean deleteUserByUsername(String username) {
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

    public Page<User> findAllUsers(Pageable pageable) {
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

        User updatedUser = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(
                () -> new ApiServiceException("Не найден пользователь с username = " + userRequest.getUsername(),
                        HttpStatus.NOT_FOUND));

        userMapper.updateUserFromDto(userRequest, updatedUser);

        return userRepository.save(updatedUser);
    }

    @Transactional
    public User persistOrUpdateUser() {
        JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        log.info("{}", token.getToken().getTokenValue());
        // String username = token.getToken().getClaimAsString("preferred_username");
        UUID userId = UUID.fromString(((Jwt) token.getPrincipal()).getSubject());
        KeyCloakUserInfoResponse keyCloakUserInfoResponse = restService.getUserInfo(token.getToken().getTokenValue());
        log.info("{}", keyCloakUserInfoResponse);
        User user = userRepository.findById(userId).orElseGet(() -> {
            return User.builder()
            .id(userId)
            .email(token.getToken().getClaim("email"))
            .username(token.getToken().getClaim("preferred_username"))
            .build();
        });

        if (user.getLastUpDateTime() != null && Duration.between(LocalDateTime.now(), user.getLastUpDateTime()).toMinutes() > 3) {
            user.setUsername(keyCloakUserInfoResponse.getUsername());
            user.setEmail(keyCloakUserInfoResponse.getEmail());
            log.info("user", user);
        }

        return userRepository.save(user);

    }

    private String getServiceAccessToken() {
        Registration registration = oauthProperties.getRegistration().get("admin");
        Provider provider = oauthProperties.getProvider().get("keycloak-admin");
        log.info(restService.getServiceAccessToken(registration, provider).getAccessToken());
        return restService.getServiceAccessToken(registration, provider).getAccessToken();
    }

    

}
