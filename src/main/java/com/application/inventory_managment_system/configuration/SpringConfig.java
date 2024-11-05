package com.application.inventory_managment_system.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class SpringConfig {

    @Bean
    RestClient restClient(){
        return RestClient.create();
    }


}
