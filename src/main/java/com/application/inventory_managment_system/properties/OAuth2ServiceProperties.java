package com.application.inventory_managment_system.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ims.oauth.service")
public class OAuth2ServiceProperties {

    private String host;

    private String realmName;

    private String clientId;

}
