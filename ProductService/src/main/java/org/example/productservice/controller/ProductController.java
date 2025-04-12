package org.example.productservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.productservice.dto.request.CreateProductRequest;
import org.example.productservice.dto.response.ApiResponse;
import org.example.productservice.dto.response.PagedResponse;
import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Product Management",
        description = "Endpoints for managing products, including creation, listing, and internal operations."
)
public class ProductController {
    ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> createProduct(@Valid @ModelAttribute CreateProductRequest request) {
        return ApiResponse.success(productService.createProduct(request), "Product created successfully");
    }

    @GetMapping("/internal/{id}")
    public ProductResponse findProductById(@PathVariable String id) {
        return productService.findProductById(id);
    }

    @GetMapping("/internal")
    public List<ProductResponse> findProductByIds(@RequestParam List<String> ids) {
        return productService.findProductByIds(ids);
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<ProductResponse> getProductBySlug(@PathVariable String slug) {
        return ApiResponse.success(productService.findProductBySlug(slug), null);
    }

    @GetMapping
    public ApiResponse<PagedResponse<ProductResponse>> getProducts(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.success(productService.getProducts(page, size), null);
    }
}
