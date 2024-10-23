package com.application.inventory_managment_system.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.inventory_managment_system.mappers.ProductMapper;
import com.application.inventory_managment_system.model.dto.request.ProductRequest;
import com.application.inventory_managment_system.model.dto.response.ProductResponse;
import com.application.inventory_managment_system.model.entities.Product;
import com.application.inventory_managment_system.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Page;


//TODO Отрефакторить апи с товарами
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Контроллер для работы с товарами", description = "API по работе с товарами")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/product")
    @Operation(
        summary = "Вывод продукта",
        description = "GET API запрос на получение товара по ID",
        responses = {
            @ApiResponse(responseCode = "404", description = "Продукт не найден", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\"message\": \"Error\", \"error\": \"Товар не найден\"}")
                })
            }),
            @ApiResponse(responseCode = "200", description = "OK")
        }
    )
    public ResponseEntity<ProductResponse> getProductById(@Parameter(description = "ID товара") @RequestParam Long id) {
        return ResponseEntity.ok(productMapper.toProductResponse(productService.getProductById(id)));
    }

    @GetMapping("/products")
    @Operation(
        summary = "Вывод продуктов",
        description = "GET API запрос на постраничный вывод продуктов",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK")
        }
    )
    public ResponseEntity<Page<ProductResponse>> findAllProducts(@ParameterObject Pageable pageable)    
    {
       return ResponseEntity.ok(
        productService.findAllProducts(pageable).map(product -> productMapper.toProductResponse(product))
       );
    }


    @PostMapping("/product/add")
    @Operation(
        summary = "Регистрация товара",
        description = "POST API запрос на регистрацию товара. В случае успеха возвращается id товара.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Товар зарегестрирован")
        })
    public Product createProduct(@RequestBody Product product) {
        
        productService.addProduct(product);
        
        return productService.getProductById(product.getId());
    }

    @PutMapping("product/update/{id}")
    public ResponseEntity<ProductResponse> putMethodName(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        
        
        return ResponseEntity.ok(
            productMapper.toProductResponse(productService.updateProductData(id, productRequest))
        );
    }



    @DeleteMapping("product/{id}")
    @Operation(
        summary = "Удаление товара",
        description = "DELETE API запрос на удаление товара по id",
        responses = {
            @ApiResponse(responseCode = "204", description = "Товар удален")
    })
    public String deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return "Удален товар с id - " + id;
    }
    

}