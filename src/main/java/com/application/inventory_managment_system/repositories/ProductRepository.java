package com.application.inventory_managment_system.repositories;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.inventory_managment_system.entities.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("from Product")
    public List<Product> findAllByPageRequest(PageRequest pageRequest);
} 
