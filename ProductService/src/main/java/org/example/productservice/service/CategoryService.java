package org.example.productservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.productservice.dto.request.CategoryRequest;
import org.example.productservice.dto.response.CategoryResponse;
import org.example.productservice.dto.response.PagedResponse;
import org.example.productservice.entity.Category;
import org.example.productservice.exception.AppException;
import org.example.productservice.exception.ErrorCode;
import org.example.productservice.mapper.CategoryMapper;
import org.example.productservice.repository.CategoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;


    @CacheEvict(value = "category", allEntries = true)
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTED);
        }
        return categoryMapper.toResponse(categoryRepository.save(categoryMapper.fromRequest(request)));
    }

    @Cacheable(value = "category", key = "'category::page::' + #page + '::size::' + #size")
    public PagedResponse<CategoryResponse> getCategories(int page, int size) {
        Page<Category> pageResult = categoryRepository.findAll(PageRequest.of(page - 1, size));

        return PagedResponse.<CategoryResponse>builder()
                .totalItems(pageResult.getTotalElements())
                .items(pageResult.getContent().stream().map(categoryMapper::toResponse).toList())
                .build();
    }
}
