package org.example.productservice.repository;

import org.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    Optional<Product> findBySlug(String slug);

    List<Product> findByIdIn(List<String> ids);

    Optional<Product> findByName(String productName);
}
