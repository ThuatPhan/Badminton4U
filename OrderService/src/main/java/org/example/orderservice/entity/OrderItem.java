package org.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_items")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String productId;

    @Column(nullable = false)
    int quantity;

    @Column(nullable = false)
    Double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;
}
