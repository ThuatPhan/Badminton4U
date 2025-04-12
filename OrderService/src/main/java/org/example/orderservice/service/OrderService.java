package org.example.orderservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.client.PaymentServiceClient;
import org.example.orderservice.client.ProductServiceClient;
import org.example.orderservice.dto.request.*;
import org.example.orderservice.dto.response.OrderItemResponse;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.dto.request.PaymentCompletedEvent;
import org.example.orderservice.dto.response.ProductResponse;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.entity.OrderStatus;
import org.example.orderservice.exception.AppException;
import org.example.orderservice.exception.ErrorCode;
import org.example.orderservice.mapper.OrderItemMapper;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.service.producer.EmailEventProducer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderService {
    ProductServiceClient productServiceClient;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderItemMapper orderItemMapper;
    EmailEventProducer emailEventProducer;
    PaymentServiceClient paymentServiceClient;

    public OrderResponse createOrder(String userId, OrderRequest orderRequest) {
        OrderResponse createdOrder = saveOrder(userId, orderRequest, OrderStatus.COD);
        sendConfirmationEmail(orderRequest.getEmail(), createdOrder);
        return createdOrder;
    }

    public String createCheckoutSession(String userId, OrderRequest orderRequest) {
        OrderResponse createdOrder = saveOrder(userId, orderRequest, OrderStatus.PENDING);
        return paymentServiceClient.createCheckoutSession(PaymentRequest.builder()
                .order(createdOrder)
                .email(orderRequest.getEmail()).build());
    }

    public void updateOrderStatus(PaymentCompletedEvent event) {
        Order orderToUpdate = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST));

        orderToUpdate.setStatus(OrderStatus.PAID);
        Order updatedOrder = orderRepository.save(orderToUpdate);

        List<String> productIds = updatedOrder.getItems().stream()
                .map(OrderItem::getProductId).collect(Collectors.toList());

        Map<String, ProductResponse> productsMap = createProductsMap(productIds);
        OrderResponse orderResponse = createOrderResponse(updatedOrder, updatedOrder.getItems(), productsMap);

        sendConfirmationEmail(event.getEmail(), orderResponse);
    }

    private OrderResponse saveOrder(String userId, OrderRequest orderRequest, OrderStatus status) {
        List<String> productIds = orderRequest.getItems().stream()
                .map(OrderItemRequest::getProductId).toList();

        List<ProductResponse> productResponses = productServiceClient.getProducts(productIds);
        Map<String, ProductResponse> productsMap = productResponses.stream()
                .collect(Collectors.toMap(ProductResponse::getId, Function.identity()));

        validateProductIds(productIds, productsMap);

        List<OrderItem> orderItems = createOrderItems(orderRequest, productsMap);

        Order newOrder = createOrder(userId, orderRequest, orderItems, status);

        orderRepository.save(newOrder);

        return createOrderResponse(newOrder, orderItems, productsMap);
    }


    private Map<String, ProductResponse> createProductsMap(List<String> productIds) {
        List<ProductResponse> productResponses = productServiceClient.getProducts(productIds);
        return productResponses.stream()
                .collect(Collectors.toMap(ProductResponse::getId, Function.identity()));
    }

    private void validateProductIds(List<String> productIds, Map<String, ProductResponse> productsMap) {
        List<String> invalidIds = productIds.stream()
                .filter(id -> !productsMap.containsKey(id))
                .toList();

        if (!invalidIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXIST);
        }
    }

    private List<OrderItem> createOrderItems(OrderRequest orderRequest, Map<String, ProductResponse> productsMap) {
        return orderRequest.getItems().stream()
                .map(itemRequest -> orderItemMapper
                        .fromRequest(itemRequest, productsMap.get(itemRequest.getProductId())))
                .collect(Collectors.toList());
    }

    private Order createOrder(String userId, OrderRequest orderRequest, List<OrderItem> orderItems, OrderStatus status) {
        Double totalAmount = calculateTotalPrice(orderItems);
        Order order = orderMapper.fromRequest(orderRequest, userId, totalAmount);
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        order.setItems(orderItems);
        order.setStatus(status);
        return order;
    }

    private List<OrderItemResponse> createOrderItemResponses(List<OrderItem> orderItems, Map<String, ProductResponse> productsMap) {
        return orderItems.stream()
                .map(orderItem -> orderItemMapper
                        .toResponse(orderItem, productsMap.get(orderItem.getProductId())))
                .collect(Collectors.toList());
    }

    private OrderResponse createOrderResponse(Order order, List<OrderItem> orderItems, Map<String, ProductResponse> productsMap) {
        List<OrderItemResponse> orderItemResponses = createOrderItemResponses(orderItems, productsMap);
        return orderMapper.toResponse(order, orderItemResponses);
    }

    private double calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getQuantity() * orderItem.getPrice()).sum();
    }

    private void sendConfirmationEmail(String email, OrderResponse order) {
        emailEventProducer.sendConfirmationEmail(SendMailEvent.builder()
                .to(email)
                .subject("Thanks for your order")
                .order(order)
                .build());
    }
}
