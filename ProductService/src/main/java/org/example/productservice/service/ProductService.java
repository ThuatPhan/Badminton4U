package org.example.productservice.service;


import com.github.slugify.Slugify;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.productservice.dto.request.CreateProductRequest;
import org.example.productservice.dto.response.PagedResponse;
import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.entity.Category;
import org.example.productservice.entity.Product;
import org.example.productservice.exception.AppException;
import org.example.productservice.exception.ErrorCode;
import org.example.productservice.mapper.ProductMapper;
import org.example.productservice.repository.CategoryRepository;
import org.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductMapper productMapper;
    CloudinaryService cloudinaryService;
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    Slugify slugify;

    @NonFinal
    @Value("${cloudinary.folder-name}")
    String folderName;

    @CacheEvict(value = "product", allEntries = true)
    public ProductResponse createProduct(CreateProductRequest request) {
        String productSlug = slugify.slugify(request.getName());
        validateNameAndSlugExists(request.getName(), productSlug);

        Category category = categoryRepository
                .findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        Product newProduct = productMapper.fromRequest(request);
        newProduct.setCategory(category);
        newProduct.setSlug(productSlug);
        newProduct.setImage(uploadImage(request.getImageFile()));

        return productMapper.toResponse(productRepository.save(newProduct));
    }

    @Cacheable(value = "product", key = "'product::page::' + #page + '::size::' + #size")
    public PagedResponse<ProductResponse> getProducts(int page, int size) {
        Page<Product> pageResult = productRepository.findAll(PageRequest.of(page - 1, size));

        return PagedResponse.<ProductResponse>builder()
                .totalItems(pageResult.getTotalElements())
                .items(pageResult.getContent().stream().map(productMapper::toResponse).toList())
                .build();
    }

    @Cacheable(value = "product", key = "'product::id::' + #id")
    public ProductResponse findProductById(String id) {
        return productMapper.toResponse(productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST)));
    }

    @Cacheable(value = "product", key = "'product::slug::' + #slug")
    public ProductResponse findProductBySlug(String slug) {
        return productMapper.toResponse(productRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST)));
    }

    @Cacheable(value = "product", key = "'product::ids::' + T(java.lang.String).join(',', #ids)")
    public List<ProductResponse> findProductByIds(List<String> ids) {
        return productRepository.findByIdIn(ids).stream().map(productMapper::toResponse).toList();
    }

    private void validateNameAndSlugExists(String name, String slug) {
        if (productRepository.existsByName(name)) {
            throw new AppException(ErrorCode.PRODUCT_NAME_ALREADY_EXISTED);
        }
        if (productRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_ALREADY_EXISTED);
        }
    }

    private String uploadImage(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinaryService.uploadFile(file, folderName);
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
