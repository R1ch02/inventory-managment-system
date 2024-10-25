package com.application.inventory_managment_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Product findProductById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ApiServiceException("Не найден продукт с id = " + id, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Product addProduct(Product product){
        if(productRepository.existsByName(product.getName())){
            throw new ApiServiceException("Товар с таким названием уже существует", HttpStatus.CONFLICT);
        }
        return productRepository.save(product);
    }

    @Transactional
    public Boolean deleteProductById(Long id){
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        } else {
            throw new ApiServiceException("Не найден продукт с id = " + id, HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    //Сравнение с самим собой?
    @Transactional
    public Product updateProductData(Long id, ProductRequest productRequest){
        if(productRepository.existsByName(productRequest.getName())){
            throw new ApiServiceException("Товар с таким названием уже существует", HttpStatus.CONFLICT);
        }
        Product updatedProduct = productRepository.findById(id)
        .orElseThrow(() -> new ApiServiceException("Не найден продукт с id = " + id, HttpStatus.NOT_FOUND));

        productMapper.updateProductFromDto(productRequest, updatedProduct);
        return updatedProduct;
    }

   

    
}
