package com.application.inventory_managment_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.inventory_managment_system.exceptions.ApiServiceException;
import com.application.inventory_managment_system.mappers.ProductMapper;
import com.application.inventory_managment_system.model.dto.request.ProductRequest;
import com.application.inventory_managment_system.model.entities.Product;
import com.application.inventory_managment_system.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Product getProductById(Long id){
        if (id == null) {
            throw new ApiServiceException("Id продукта не может быть null", HttpStatus.BAD_REQUEST);
        }
        
        return productRepository.findById(id).orElseThrow(() -> new ApiServiceException("Продукт не найден", HttpStatus.NOT_FOUND));
    }


    public void addProduct(Product product){
        productRepository.save(product);
    }

    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }

    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product updateProductData(Long id, ProductRequest productRequest){
        Product updatedProduct = productRepository.findById(id)
        .orElseThrow(() -> new ApiServiceException("Продукт не найден", HttpStatus.NOT_FOUND));

        productMapper.updateProductFromDto(productRequest, updatedProduct);
        return updatedProduct;
    }

   

    
}
