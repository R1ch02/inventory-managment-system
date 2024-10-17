package com.application.inventory_managment_system.entities.dto;

import org.springframework.stereotype.Component;


public record UserResponse(long id, String username, String email) {
}
