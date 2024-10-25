package com.application.inventory_managment_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.inventory_managment_system.model.entities.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsByName(String name);

} 
