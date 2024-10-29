package com.application.inventory_managment_system.services;

import java.net.URI;
import java.util.List;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

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

    ResponseEntity<KeyCloakUserInfoResponse> getUserInfo(String token){
        URI uri = UriComponentsBuilder.fromHttpUrl(serviceProperties.getHost())
            .path("/realms/{realmName}/protocol/openid-connect/userinfo")
            .buildAndExpand("ims-realm")
            .toUri();
    
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
            .toEntity(KeyCloakUserInfoResponse.class);
        
    }

    private HttpHeaders getHttpHeaders(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        return httpHeaders;
    }
}
