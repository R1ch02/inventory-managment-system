package com.application.inventory_managment_system.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.inventory_managment_system.mappers.ProductMapper;
import com.application.inventory_managment_system.model.dto.request.ProductRequest;
import com.application.inventory_managment_system.model.dto.response.MessageResponse;
import com.application.inventory_managment_system.model.dto.response.ProductResponse;
import com.application.inventory_managment_system.model.view.ProductView;
import com.application.inventory_managment_system.services.ProductService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
@Tag(name = "Контроллер для работы с товарами", description = "API по работе с товарами")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/product")
    @Operation(
        summary = "Вывод продукта",
        description = "GET API запрос на получение товара по ID",
        responses = {
            @ApiResponse(responseCode = "404", description = "Товар не найден", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\"message\": \"Error\", \"error\": \"Товар не найден\"}")
                })
            }),
            @ApiResponse(responseCode = "200", description = "OK")
        }
    )
    public ResponseEntity<ProductResponse> getProductById(@Parameter(description = "ID товара") @Positive @RequestParam Long id) {
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(productMapper.toProductResponse(productService.findProductById(id)));
    }

    @GetMapping("/products")
    @Operation(
        summary = "Вывод продуктов",
        description = "GET API запрос на постраничный вывод продуктов",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK")
        }
    )
    public ResponseEntity<Page<ProductResponse>> findAllProducts(@ParameterObject Pageable pageable)  {
       return ResponseEntity
       .status(HttpStatus.OK)
       .body(productService.findAllProducts(pageable).map(product -> productMapper.toProductResponse(product)));
    }


    @PostMapping("/product/add")
    @Operation(
        summary = "Регистрация товара",
        description = "POST API запрос на регистрацию товара.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Товар зарегестрирован")
        })
    public ResponseEntity<MessageResponse> createProduct(@RequestBody @Validated @JsonView(ProductView.CreateProduct.class) ProductRequest productRequest) {
        
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new MessageResponse(
            productService.addProduct(
                productMapper.toProduct(productRequest)
            )
        ));

    }

    @PutMapping("product/update/{id}")
    @Operation(
        summary = "Редактирование товара",
        description = "PUT API запрос на редактирование товара",
        responses = {
            @ApiResponse(responseCode = "202", description = "Товар зарегестрирован")
        }
    )
    public ResponseEntity<ProductResponse> putMethodName(@PathVariable @Positive Long id, @RequestBody @Validated @JsonView(ProductView.UpdateProductData.class) ProductRequest productRequest) {
    
        return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(
            productMapper.toProductResponse(productService.updateProductData(id, productRequest))
        );
    }



    @DeleteMapping("product/delete/{id}")
    @Operation(
        summary = "Удаление товара",
        description = "DELETE API запрос на удаление товара по id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Товар удален")
        }
    )
    public ResponseEntity<String> deleteProductById(@PathVariable @Parameter(description = "ID товара") @Validated @Positive Long id){
        
        return ResponseEntity
        .status(HttpStatus.OK)
        .body("Товар с id '" + id + "' удален: " + productService.deleteProductById(id)
        );
    }
    

}