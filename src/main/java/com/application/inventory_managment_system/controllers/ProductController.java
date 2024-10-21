package com.application.inventory_managment_system.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.inventory_managment_system.entities.Product;
import com.application.inventory_managment_system.services.ProductService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


//Добавить валидацию
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<Product> getProductById(@RequestParam Long id) {
        
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAllProducts(@RequestParam(required = false, defaultValue = "0") int page, 
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
       return ResponseEntity.ok(productService.findAllByPageRequest(PageRequest.of(page, size)));
    }

    @PostMapping("/product/add")
    public Product postMethodName(@RequestBody Product product) {
        
        productService.addProduct(product);
        
        return productService.getProductById(product.getId());
    }

    @DeleteMapping("product/{id}")
    public String deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return "Удален продукет с id - " + id;
    }
    
    
    
}