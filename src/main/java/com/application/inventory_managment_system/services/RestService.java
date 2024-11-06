package com.application.inventory_managment_system.services;

import java.net.URI;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Provider;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.application.inventory_managment_system.exceptions.ApiServiceException;
import com.application.inventory_managment_system.model.dto.request.keycloak.KeyCloakUserRepresentation;
import com.application.inventory_managment_system.model.dto.response.keycloak.KeyCloakJwtResponse;
import com.application.inventory_managment_system.model.dto.response.keycloak.KeyCloakUserInfoResponse;
import com.application.inventory_managment_system.properties.OAuth2ServiceProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestService {

    private final RestClient restClient;
    private final OAuth2ServiceProperties serviceProperties;

    public KeyCloakUserInfoResponse getUserInfo(String token) {
        URI uri = UriComponentsBuilder.fromHttpUrl(serviceProperties.getHost())
                .path("/realms/{realmName}/protocol/openid-connect/userinfo")
                .buildAndExpand("ims-realm")
                .toUri();

        log.info("{}", uri);

        return restClient
                .get()
                .uri(uri)
                .headers(headers -> headers.addAll(getHttpHeaders(token)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        status -> status.is2xxSuccessful(),
                        (request, response) -> {
                            log.debug("{} - {} - {}", request.getMethod(), request.getURI(), response.getStatusCode());
                        })
                .toEntity(KeyCloakUserInfoResponse.class)
                .getBody();

    }

    private HttpHeaders getHttpHeaders(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        log.info("{}", httpHeaders);
        return httpHeaders;
    }

    ResponseEntity<Void> createUser(String accessToken, KeyCloakUserRepresentation newUser) {
        URI uri = UriComponentsBuilder.fromHttpUrl(serviceProperties.getHost())
                .path("/admin/realms/{realmName}/users")
                .buildAndExpand("ims-realm")
                .toUri();
        log.info("{}", uri);
        return restClient
                .post()
                .uri(uri)
                .headers(headers -> headers.addAll(getHttpHeaders(accessToken)))
                .contentType(MediaType.APPLICATION_JSON)
                .body(newUser)
                .retrieve()
                .onStatus(
                        status -> status.value() == 409,
                        (request, response) -> {
                            throw new ApiServiceException("Данная почта уже занята", HttpStatus.CONFLICT);
                        })
                .onStatus(
                        status -> status.is2xxSuccessful(),
                        (request, response) -> {
                            log.debug("{} - {} - {}", request.getMethod(), request.getURI(), response.getStatusCode());
                        })
                .toBodilessEntity();
    }

    KeyCloakJwtResponse getServiceAccessToken(Registration clientRegistration, Provider clientProvider) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientRegistration.getClientId());
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("client_secret", clientRegistration.getClientSecret());
        log.debug("{}", requestBody);

        return restClient
            .post()
            .uri(clientProvider.getTokenUri())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(requestBody)
            .retrieve()
            .onStatus(
                status -> status.is2xxSuccessful(),
                (request, response) -> {
                    log.debug("{} - {} - {}", request.getMethod(), request.getURI(), response.getStatusCode());
            })
            .body(KeyCloakJwtResponse.class);
            
    }
}
