package org.example.cartservice.repository;

import org.example.cartservice.entity.Cart;
import org.example.cartservice.entity.CartItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCartIdAndProductId(String cartId, String productId);

    Page<CartItem> findAllByCart(Cart cart, Pageable pageable);
}
