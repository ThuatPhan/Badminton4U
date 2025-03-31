package org.example.productservice.mapper;

import org.example.productservice.dto.request.CreateProductRequest;
import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product fromRequest(CreateProductRequest request);

    ProductResponse toResponse(Product product);
}
