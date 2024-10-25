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
            @ApiResponse(responseCode = "400", description = "Введен невалидный id товар", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": {\r\n" + //
                                                "    \"findProductById.id\": \"Id должно быть больше 0\"\r\n" + //
                                                "  }\r\n" + //
                                                "}")
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
        @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = {
            @Content(examples = {
                @ExampleObject(name = "Остутствуют поля", value = "{\r\n" + //
                                            "  \"message\": \"Error\",\r\n" + //
                                            "  \"error\": \"Некорректное тело запроса. Не обозначены обязательные поля\"\r\n" + //
                                            "}"),
                @ExampleObject(name = "Не валидные поля", value = "{\r\n" + //
                                        "  \"message\": \"Error\",\r\n" + //
                                        "  \"error\": {\r\n" + //
                                        "    \"quantity\": \"должно быть больше или равно 0\",\r\n" + //
                                        "    \"price\": \"должно быть больше 0\",\r\n" + //
                                        "    \"name\": \"не должно быть пустым\"\r\n" + //
                                        "  }\r\n" + //
                                        "}")
            })
        }),
        @ApiResponse(responseCode = "409", description = "Товар с уникальным типом данных уже существует", content = {
            @Content(examples = {
                @ExampleObject(name = "Логин занят", value = "{\r\n" + //
                                            "  \"message\": \"Error\",\r\n" + //
                                            "  \"error\": \"Товар с таким названием уже существует\"\r\n" + //
                                            "}")
            })
        }),
        @ApiResponse(responseCode = "201", description = "Товар зарегестрирован", content = {
            @Content(examples = {
                @ExampleObject(value =  "{\r\n" + //
                                        "  \"message\": {\r\n" + //
                                        "    \"id\": 1,\r\n" + //
                                        "    \"name\": \"Круасан\",\r\n" + //
                                        "    \"description\": \"Круасан 7 DAYS с клубничной начинкой\",\r\n" + //
                                        "    \"price\": 79.99,\r\n" + //
                                        "    \"quantity\": 20,\r\n" + //
                                        "    \"creationDateTime\": \"2024-10-25T09:18:10.196776\",\r\n" + //
                                        "    \"lastUpDateTime\": \"2024-10-25T09:18:10.19783\"\r\n" + //
                                        "  },\r\n" + //
                                        "  \"error\": null\r\n" + //
                                        "}")
            })
        })
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
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = {
                @Content(examples = {
                    @ExampleObject(name = "Отсутствуют поля", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": \"Некорректное тело запроса. Не обозначены обязательные поля\"\r\n" + //
                                                "}"),
                    @ExampleObject(name = "Id > 0",value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": {\r\n" + //
                                                "    \"putMethodName.id\": \"Id должно быть больше 0\"\r\n" + //
                                                "  }\r\n" + //
                                                "}"),
                    @ExampleObject(name = "Не валидные поля", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": {\r\n" + //
                                                "    \"quantity\": \"должно быть больше или равно 0\",\r\n" + //
                                                "    \"price\": \"должно быть больше 0\",\r\n" + //
                                                "    \"name\": \"не должно быть пустым\"\r\n" + //
                                                "  }\r\n" + //
                                                "}")
                })
            }),
            @ApiResponse(responseCode = "404", description = "Товар не найден", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\"message\": \"Error\", \"error\": \"Товар не найден\"}")
                })
            }),
            @ApiResponse(responseCode = "409", description = "Товар с уникальным типом данных уже существует", content = {
                @Content(examples = {
                    @ExampleObject(name = "Товар существует", value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": \"Товар с таким названием уже существует\"\r\n" + //
                                                "}")
                })
            }),
            @ApiResponse(responseCode = "202", description = "Товар отредактирован", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\r\n" + //
                                                "  \"id\": 1,\r\n" + //
                                                "  \"name\": \"Круасан\",\r\n" + //
                                                "  \"description\": \"Круасан 7 DAYS с ванильной начинкой\",\r\n" + //
                                                "  \"price\": 60.01,\r\n" + //
                                                "  \"quantity\": 10\r\n" + //
                                                "}")
                })
            })
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
            @ApiResponse(responseCode = "404", description = "Товар не найден", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": \"Не найден товар с id = 2\"\r\n" + //
                                                "}")
                })
            }),
            @ApiResponse(responseCode = "400", description = "Введен невалидный id товара", content = {
                @Content(examples = {
                    @ExampleObject(value = "{\r\n" + //
                                                "  \"message\": \"Error\",\r\n" + //
                                                "  \"error\": {\r\n" + //
                                                "    \"findProductById.id\": \"Id должно быть больше 0\"\r\n" + //
                                                "  }\r\n" + //
                                                "}")
                })
            }),
            @ApiResponse(responseCode = "200", description = "Товар удален", content = {
                @Content(examples = {
                    @ExampleObject(value = "Товар с id '1' удален: true")
                })
            })
    }
    )
    public ResponseEntity<String> deleteProductById(@PathVariable @Parameter(description = "ID товара") @Validated @Positive Long id){
        
        return ResponseEntity
        .status(HttpStatus.OK)
        .body("Товар с id '" + id + "' удален: " + productService.deleteProductById(id)
        );
    }
    

}