package com.application.inventory_managment_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.inventory_managment_system.model.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {


}
