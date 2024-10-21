package com.application.inventory_managment_system.repositories;

import com.application.inventory_managment_system.entities.User;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("from User")
    List<User> findAllByPageRequest(PageRequest pageRequest);
}
