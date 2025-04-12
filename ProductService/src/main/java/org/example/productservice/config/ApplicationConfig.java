package org.example.productservice.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.productservice.entity.Category;
import org.example.productservice.entity.Product;
import org.example.productservice.repository.CategoryRepository;
import org.example.productservice.repository.ProductRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationConfig {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            String categoryName = "Racket";
            String productName = "Astrox 100zz Dark Navy";

            Category category = categoryRepository.findByName(categoryName).orElseGet(() ->
                    categoryRepository.save(Category.builder().name(categoryName).build()));

            productRepository.findByName(productName).orElseGet(() ->
                    productRepository.save(Product.builder()
                            .name(productName)
                            .description("A racket from Yonex brand")
                            .price(350.0)
                            .category(category)
                            .slug("astrox-100zz-dark-navy")
                            .image("https://res.cloudinary.com/dmz26yafu/image/upload/v1744357735/Uploads/plyocicvsrqtyj7qrmug.jpg")
                            .build()));

        };
    }
}
