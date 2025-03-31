package org.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String userId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items;

    @Column(nullable = false)
    Double totalAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OrderStatus status = OrderStatus.COD;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;
}
