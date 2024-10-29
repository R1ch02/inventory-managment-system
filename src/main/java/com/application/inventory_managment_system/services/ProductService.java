package com.application.inventory_managment_system.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.inventory_managment_system.exceptions.ApiServiceException;
import com.application.inventory_managment_system.mappers.ProductMapper;
import com.application.inventory_managment_system.model.dto.request.ProductRequest;
import com.application.inventory_managment_system.model.entities.Product;
import com.application.inventory_managment_system.model.entities.User;
import com.application.inventory_managment_system.repositories.ProductRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserService userService;

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
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            for (User user : product.get().getUsers()) {
                user.getProducts().remove(product);
            }
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

    @Transactional
    public Product buyProductByName(@NotBlank String name) {
        Product product = productRepository.findByName(name).orElseThrow(() -> new ApiServiceException("Товар с таким названием не найден", HttpStatus.NOT_FOUND));
        product.setQuantity(product.getQuantity()-1);
        product.getUsers().add(userService.getUserFromSecurityContext());
        return productRepository.save(product);
    }

   

    
}
