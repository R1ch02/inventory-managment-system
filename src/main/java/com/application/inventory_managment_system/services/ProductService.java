package com.application.inventory_managment_system.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.inventory_managment_system.entities.Product;
import com.application.inventory_managment_system.exceptions.ApiServiceException;
import com.application.inventory_managment_system.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
//TODO Добавить валидацию
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(Long id){
        if (id == null) {
            throw new ApiServiceException("Id продукта не может быть null", HttpStatus.BAD_REQUEST);
            
        }
        return productRepository.findById(id).orElseThrow(() -> new ApiServiceException("Продукт не найден", HttpStatus.NOT_FOUND));
    }

    public List<Product> findAllByPageRequest(PageRequest pageRequest) {
        return productRepository.findAllByPageRequest(pageRequest);
    }

    public void addProduct(Product product){
        productRepository.save(product);
    }

    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }

   

    
}
