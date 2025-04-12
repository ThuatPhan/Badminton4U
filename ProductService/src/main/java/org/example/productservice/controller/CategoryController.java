package org.example.productservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.productservice.dto.request.CategoryRequest;
import org.example.productservice.dto.response.ApiResponse;
import org.example.productservice.dto.response.CategoryResponse;
import org.example.productservice.dto.response.PagedResponse;
import org.example.productservice.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(
        name = "Category Management",
        description = "Endpoints for managing product categories, including creation, listing, and retrieval."
)public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.success(categoryService.createCategory(categoryRequest), "Category created successfully");
    }

    @GetMapping
    public ApiResponse<PagedResponse<CategoryResponse>> getCategories(int page, int size) {
        return ApiResponse.success(categoryService.getCategories(page, size), null);
    }
}
