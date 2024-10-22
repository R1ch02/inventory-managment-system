package com.application.inventory_managment_system.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.application.inventory_managment_system.model.dto.response.ProductResponse;
import com.application.inventory_managment_system.model.entities.Product;


import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
    ) 
public interface ProductMapper{
    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);
} 
